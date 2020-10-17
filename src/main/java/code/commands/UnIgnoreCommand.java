package code.commands;

import code.BasicMsg;
import code.CacheManager;
import code.Manager;
import code.modules.IgnoreMethod;
import code.registry.ConfigManager;
import code.utils.Configuration;
import code.modules.PlayerMessage;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UnIgnoreCommand implements CommandClass{


    private final BasicMsg plugin;
    private final Manager manager;
    private final CacheManager cache;

    public UnIgnoreCommand(BasicMsg plugin, Manager manager, CacheManager cache){
        this.plugin = plugin;
        this.manager = manager;
        this.cache = cache;

    }
    @Command(names = "unignore")
    public boolean onCommand( CommandSender sender, @OptArg OfflinePlayer target) {

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

        if (target == null){
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            return true;
        }
        if (!(target.isOnline())){
            playersender.sendMessage(sender, messages.getString("error.player-offline"));
            return true;
        }
        if (target.getName().equalsIgnoreCase(sender.getName())){
            playersender.sendMessage(sender, messages.getString("error.ignore.ignore-yourself"));
            return true;
        }

        String targetname = target.getName();
        UUID playeruuid = player.getUniqueId();

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();

        if (!(ignorelist.containsKey(playeruuid))){
            playersender.sendMessage(sender, messages.getString("error.ignore.anybody"));
            return true;
        }

        List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

        if (!(ignoredlist.contains(targetname))){
            playersender.sendMessage(sender, messages.getString ("error.ignore.already-unignored"));
            return true;
        }
            ignore.remove(sender, target.getPlayer());
            playersender.sendMessage(sender, command.getString("commands.ignore.player-unignored").replace("%player%", targetname));

        return true;
    }
}
