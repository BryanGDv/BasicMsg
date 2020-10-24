package code.commands;

import code.CacheManager;
import code.modules.IgnoreMethod;
import code.registry.ConfigManager;
import code.modules.PlayerMessage;
import code.sounds.SoundManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;


import me.fixeddev.commandflow.annotated.annotation.OptArg;
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
    public boolean ignore(CommandSender sender, @OptArg OfflinePlayer target) {

        ConfigManager files = manager.getFiles();
        IgnoreMethod ignore = manager.getPlayerMethods().getIgnoreMethod();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();


        SoundManager sound = manager.getSounds();

        Configuration players = files.getPlayers();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;
        UUID playeruuid = player.getUniqueId();

        if (target == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/ignore [player]");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (!(target.isOnline()) && (!(target.getName().equals("-list")))) {
            playersender.sendMessage(sender, messages.getString("error.player-offline"));
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (target.getName().equalsIgnoreCase(player.getName())) {
            playersender.sendMessage(sender, messages.getString("error.ignore.ignore-yourself"));
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();
        List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

        if (target.getName().equalsIgnoreCase("-list")) {
            playersender.sendMessage(player, command.getString("commands.ignore.space"));

            if (ignorelist.containsKey(playeruuid)) {

                if (ignoredlist == null){
                    playersender.sendMessage(player, messages.getString("error.ignore.anybody"));
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
            sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-list");
            return true;
        }

        String targetname = target.getPlayer().getName();

        if (ignorelist.containsKey(playeruuid)) {

            if (!(ignoredlist.contains(targetname))) {
                ignore.set(sender, target.getUniqueId());
                playersender.sendMessage(sender, command.getString("commands.ignore.player-ignored").replace("%player%", targetname));
                sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-ignore");
            } else {
                ignore.unset(sender, target.getUniqueId());
                playersender.sendMessage(sender, command.getString("commands.ignore.player-unignored").replace("%player%", targetname));
                sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-unignore");
            }
            return true;
        }else{
            ignore.set(sender, target.getUniqueId());
            playersender.sendMessage(sender, command.getString("commands.ignore.player-ignored").replace("%player%", targetname));
            sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-ignore");
        }
        return true;
        }


}
