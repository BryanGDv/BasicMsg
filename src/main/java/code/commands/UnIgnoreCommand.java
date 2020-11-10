package code.commands;

import code.CacheManager;
import code.Manager;
import code.modules.IgnoreMethod;
import code.registry.ConfigManager;
import code.bukkitutils.SoundManager;
import code.utils.Configuration;
import code.modules.player.PlayerMessage;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UnIgnoreCommand implements CommandClass{


    private final Manager manager;
    private final CacheManager cache;

    public UnIgnoreCommand(Manager manager, CacheManager cache){
        this.manager = manager;
        this.cache = cache;

    }
    @Command(names = "unignore")
    public boolean onCommand(@Sender Player player, @OptArg OfflinePlayer target) {

        ConfigManager files = manager.getFiles();
        IgnoreMethod ignore = manager.getPlayerMethods().getIgnoreMethod();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        SoundManager sound = manager.getSounds();

        Configuration players = files.getPlayers();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(manager.getPathManager().isCommandEnabled("unignore"))){
            playersender.sendMessage(player, messages.getString("error.command-disabled")
                    .replace("%player%", player.getName())
                    .replace("%command%", "unignore"));
            playersender.sendMessage(player, "&e[!] &8| &fYou need to restart the server to activate o unactivate the command.");
            return true;
        }

        if (target == null){
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }
        if (!(target.isOnline())){
            playersender.sendMessage(player, messages.getString("error.player-offline"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }
        if (target.getName().equalsIgnoreCase(player.getName())){
            playersender.sendMessage(player, messages.getString("error.ignore.ignore-yourself"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }

        String targetname = target.getName();
        UUID playeruuid = player.getUniqueId();

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();

        if (!(ignorelist.containsKey(playeruuid))){
            playersender.sendMessage(player, messages.getString("error.ignore.anybody"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }

        List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");
        UUID targetuuid = target.getPlayer().getUniqueId();

        if (!(ignoredlist.contains(targetname))){
            playersender.sendMessage(player, messages.getString ("error.ignore.already-unignored"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }
            ignore.unset(player, targetuuid);
            playersender.sendMessage(player, command.getString("commands.ignore.player-unignored").replace("%player%", targetname));
            sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-unignore");
        return true;
    }
}
