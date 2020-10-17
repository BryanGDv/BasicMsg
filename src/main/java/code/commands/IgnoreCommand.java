package code.commands;

import code.CacheManager;
import code.modules.IgnoreMethod;
import code.registry.ConfigManager;
import code.modules.PlayerMessage;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;


import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;
import code.Manager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IgnoreCommand implements CommandClass{

    private final BasicMsg plugin;
    private final Manager manager;
    private final CacheManager cache;

    public IgnoreCommand(BasicMsg plugin, Manager manager, CacheManager cache){
        this.plugin = plugin;
        this.manager = manager;
        this.cache = cache;
    }

    @Command(names = "ignore")
    public boolean ignore(CommandSender sender, @OptArg OfflinePlayer target) {

        ConfigManager files = manager.getFiles();
        IgnoreMethod ignore = manager.getPlayerMethods().getIgnoreMethod();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration players = files.getPlayers();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(sender instanceof Player)){
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;
        UUID playeruuid = player.getUniqueId();

        if (target == null){
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/ignore [player]");
            return true;
        }

        if (!(target.isOnline())){
            playersender.sendMessage(sender, messages.getString("error.player-offline"));
            return true;
        }

        if (target.getName().equalsIgnoreCase(player.getName())) {
            playersender.sendMessage(sender, messages.getString("error.ignore.ignore-yourself"));
            return true;
        }

        String targetname = target.getPlayer().getName();

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();
        if (ignorelist.containsKey(playeruuid)){

            List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

            if (!(ignoredlist.contains(targetname))){
                ignore.add(sender, target.getPlayer());
                playersender.sendMessage(sender, command.getString("commands.ignore.player-ignored").replace("%player%", targetname));
            }else{
                ignore.remove(sender, target.getPlayer());
                playersender.sendMessage(sender, command.getString("commands.ignore.player-unignored").replace("%player%", targetname));

            }
            return true;
        } else {
            ignore.add(sender, target.getPlayer());
            playersender.sendMessage(sender, command.getString("commands.ignore.player-ignored").replace("%player%", targetname));
        }
        return true;

    }
}
