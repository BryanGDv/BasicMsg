package code.commands;

import code.CacheManager;
import code.modules.IgnoreMethod;
import code.registry.ConfigManager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;


import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.utils.Configuration;
import code.Manager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IgnoreCommand implements CommandClass{

    private final Manager manager;
    private final CacheManager cache;

    public IgnoreCommand(Manager manager, CacheManager cache){
        this.manager = manager;
        this.cache = cache;
    }

    @Command(names = "ignore")
    public boolean ignore(@Sender Player player, @OptArg OfflinePlayer target) {

        ConfigManager files = manager.getFiles();
        IgnoreMethod ignore = manager.getPlayerMethods().getIgnoreMethod();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getSounds();

        Configuration players = files.getPlayers();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();


        UUID playeruuid = player.getUniqueId();

        if (!(manager.getPathManager().isCommandEnabled("ignore"))){
            playersender.sendMessage(player, messages.getString("error.command-disabled")
                    .replace("%player%", player.getName())
                    .replace("%command%", "ignore"));
            playersender.sendMessage(player, "&e[!] &8| &fYou need to restart the server to activate o unactivate the command.");
            return true;
        }


        if (target == null) {
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            playersender.sendMessage(player, "&8- &fUsage: &a/ignore [player]");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (!(target.isOnline()) && (!(target.getName().equals("-list")))) {
            playersender.sendMessage(player, messages.getString("error.player-offline"));
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (target.getName().equalsIgnoreCase(player.getName())) {
            playersender.sendMessage(player, messages.getString("error.ignore.ignore-yourself"));
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();
        List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

        if (target.getName().equalsIgnoreCase("-list")) {
            playersender.sendMessage(player, command.getString("commands.ignore.space"));

            if (ignorelist.containsKey(playeruuid)) {

                if (ignoredlist == null){
                    playersender.sendMessage(player, command.getString("commands.ignore.nobody-ignored"));
                } else {
                    playersender.sendMessage(player, command.getString("commands.ignore.list-ignoredplayers"));
                    for (String playersignored : ignoredlist) {
                        playersender.sendMessage(player, "&8- &a" + playersignored);
                    }
                }
            } else {
                playersender.sendMessage(player, messages.getString("error.ignore.anybody"));

            }
            playersender.sendMessage(player, command.getString("commands.ignore.space"));
            sound.setSound(playeruuid, "sounds.on-list");
            return true;
        }

        String targetname = target.getPlayer().getName();

        if (ignorelist.containsKey(playeruuid)) {

            if (!(ignoredlist.contains(targetname))) {
                ignore.set(player, target.getUniqueId());
                playersender.sendMessage(player, command.getString("commands.ignore.player-ignored").replace("%player%", targetname));
                sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-ignore");
            } else {
                ignore.unset(player, target.getUniqueId());
                playersender.sendMessage(player, command.getString("commands.ignore.player-unignored").replace("%player%", targetname));
                sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-unignore");
            }
            return true;
        }else{
            ignore.set(player, target.getUniqueId());
            playersender.sendMessage(player, command.getString("commands.ignore.player-ignored").replace("%player%", targetname));
            sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-ignore");
        }
        return true;
        }


}
