package code.commands;

import code.CacheManager;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.PlayerMessage;
import code.modules.Color;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;

import java.util.Map;
import java.util.UUID;

public class ReplyCommand implements CommandClass {

    private final BasicMsg plugin;
    private final Manager manager;
    private final CacheManager cache;

    public ReplyCommand(BasicMsg plugin, Manager manager, CacheManager cache){
        this.plugin = plugin;
        this.manager = manager;
        this.cache = cache;
    }
    @Command(names = {"reply", "r"})
    public boolean onCommand(CommandSender sender, @OptArg @Text String message) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration config = files.getCommand();
        Configuration command = files.getCommand();
        Configuration lang = files.getMessages();

        if(!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(lang.getString("error.console")));
            return true;
        }

        if (message.trim().isEmpty()) {
            playersender.sendMessage(sender, lang.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/reply [message]");
            return true;
        }

        Player player = (Player) sender;
        Map<UUID, UUID> reply = cache.getReply();

        if (cache.getReply().containsKey(player.getUniqueId())) {

            OfflinePlayer target = Bukkit.getPlayer(reply.get(player.getUniqueId()));

            CommandSender targetsender = target.getPlayer();

            String msg;

            if (sender.hasPermission(config.getString("config.perms.color"))){
                msg = ChatColor.stripColor(message);
            }else{
                msg = Color.color(message);
            }

            playersender.sendMessage(player, Color.color(command.getString ("commands.msg-reply.player")
                    .replace("%player%", sender.getName())
                    .replace("%arg-1%", target.getName()))
                    .replace("%message%", msg), false);

            playersender.sendMessage(targetsender, Color.color(command.getString ("commands.msg-reply.player")
                    .replace("%player%", sender.getName())
                    .replace("%arg-1%", target.getName()))
                    .replace("%message%", msg), false);

            reply.replace(reply.get(player.getUniqueId()), player.getUniqueId());


        } else {
            playersender.sendMessage(sender, lang.getString("error.no-reply"));

        }
        return true;
    }
}


