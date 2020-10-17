package code.commands;

import code.commands.cache.CacheManager;
import code.commands.cache.ReplyCache;
import code.registry.ConfigManager;
import code.modules.PlayerMessage;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;
import code.Manager;

import java.util.Map;
import java.util.UUID;

public class MsgCommand implements CommandClass{

    private final BasicMsg plugin;
    private final Manager manager;
    private final CacheManager cache;

    public MsgCommand(BasicMsg plugin, Manager manager, CacheManager cache){
        this.plugin = plugin;
        this.manager = manager;
        this.cache = cache;
    }

    @Command(names = "msg")
    public boolean onCommand(CommandSender sender, @OptArg OfflinePlayer target, @OptArg @Text String message) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();


        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }
        if (target == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/msg [player] [message]");
            return true;
        }
        Player player = (Player) sender;
        if (!(target.isOnline())) {
            playersender.sendMessage(sender, messages.getString("error.player-offline"));
            return true;
        }

        if (target.getName().equalsIgnoreCase(sender.getName())) {
            playersender.sendMessage(sender, messages.getString("error.same-player")
                    .replace("%player%", sender.getName()));
            return true;

        }

        if (message.trim().isEmpty()) {
            playersender.sendMessage(sender, messages.getString( "error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/msg [player] [message]");
            return true;
        }
        playersender.sendMessage(player, command.getString("commands.msg-reply.player")
                .replace("%player%", sender.getName())
                .replace("%arg-1%", target.getName())
                .replace("%message%", message));

        playersender.sendMessage(target.getPlayer(), command.getString("commands.msg-reply.arg-1")
                .replace("%player%", sender.getName())
                .replace("%arg-1%", target.getName())
                .replace("%message%", message));

        for (Player watcher : Bukkit.getServer().getOnlinePlayers()) {
            if (cache.getSocialSpy().get().contains(watcher.getUniqueId())) {
                playersender.sendMessage(watcher, command.getString ("commands.socialspy.spy")
                        .replace("%player%", sender.getName())
                        .replace("%arg-1%", target.getName())
                        .replace("%message%", message));
            }
        }

        Map<UUID, UUID> reply = cache.getReply().get();

        if (!(reply.containsKey(player.getUniqueId()))) {
            reply.put(player.getUniqueId(), target.getUniqueId());
            reply.put(target.getUniqueId(), player.getUniqueId());
        } else {
            reply.replace(player.getUniqueId(), target.getUniqueId());
            reply.replace(target.getUniqueId(), player.getUniqueId());
        }

            return true;
        }
}
