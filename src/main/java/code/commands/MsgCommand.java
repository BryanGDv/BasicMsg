package code.commands;

import code.CacheManager;
import code.modules.MsgToggleMethod;
import code.registry.ConfigManager;
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
import code.Manager;

import java.util.Map;
import java.util.Set;
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

    @Command(names = {"msg", "pm", "tell", "t"})
    public boolean onCommand(CommandSender sender, @OptArg OfflinePlayer target, @OptArg @Text String message) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        MsgToggleMethod msgToggleMethod = manager.getPlayerMethods().getMsgToggleMethod();

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

        if (!(target.isOnline()) && (!(target.getName().equalsIgnoreCase("-toggle")))){
            playersender.sendMessage(sender, messages.getString("error.player-offline"));
            return true;
        }

        if (target.getName().equalsIgnoreCase(sender.getName())) {
            playersender.sendMessage(sender, messages.getString("error.same-player")
                    .replace("%player%", sender.getName()));
            return true;

        }

        if (target.getName().equalsIgnoreCase("-toggle")){
            Set<UUID> msgtoggle = cache.getMsgToggle();
            if (message.trim().isEmpty()){

                if (!(msgtoggle.contains(player.getUniqueId()))){
                    msgToggleMethod.on(player.getUniqueId());
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.player.on"));
                }else{
                    msgToggleMethod.off(player.getUniqueId());
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.player.off"));
                }

            } else {

                OfflinePlayer you = Bukkit.getPlayer(message);
                CommandSender yousender =  you.getPlayer();

                if (!(you.isOnline())){
                    playersender.sendMessage(sender, messages.getString("error.player-offline"));
                    return true;
                }

                if (!(msgtoggle.contains(you.getUniqueId()))){
                    msgToggleMethod.on(you.getUniqueId());
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.player..on")
                            .replace("%arg-1%", yousender.getName()));
                    playersender.sendMessage(yousender, command.getString("commands.msg-toggle.arg-1.on"));
                }else{
                    msgToggleMethod.off(you.getUniqueId());
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.player.off")
                            .replace("%arg-1", you.getPlayer().getName()));
                    playersender.sendMessage(yousender, command.getString("commands.msg-toggle.arg-1.off"));
                }
                return true;
            }
            return true;
        }
        if (message.trim().isEmpty()) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/msg [player] [message]");
            return true;
        }


        if (sender.hasPermission(config.getString("config.perms.color"))){
           message = Color.color(message);
        }

        String playerFormat = Color.color(command.getString("commands.msg-reply.player"));
        String targetFormat = Color.color(command.getString("commands.msg-reply.arg-1"));

        playersender.sendMessage(player, playerFormat

                .replace("%player%", sender.getName())
                .replace("%arg-1%", target.getName())
                , true,  message);

        playersender.sendMessage(target.getPlayer(), targetFormat

                .replace("%player%", sender.getName())
                .replace("%arg-1%", target.getName())
                , true, message);

        for (Player watcher : Bukkit.getServer().getOnlinePlayers()) {
            if (cache.getSocialSpy().contains(watcher.getUniqueId())) {
                playersender.sendMessage(watcher, command.getString ("commands.socialspy.spy")
                        .replace("%player%", sender.getName())
                        .replace("%arg-1%", target.getName())
                        .replace("%message%", message));
            }
        }

        Map<UUID, UUID> reply = cache.getReply();

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
