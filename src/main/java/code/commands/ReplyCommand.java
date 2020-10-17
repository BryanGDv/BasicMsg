package code.commands;

import code.CacheManager;
import code.registry.ConfigManager;
import code.Manager;
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
    @Command(names = "reply")
    public boolean onCommand(CommandSender sender, @OptArg @Text String messages) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration command = files.getCommand();
        Configuration lang = files.getMessages();

        if(!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(lang.getString("error.console")));
            return true;
        }

        if (messages.trim().isEmpty()) {
            playersender.sendMessage(sender, lang.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/reply [message]");
            return true;
        }

        Player player = (Player) sender;
        Map<UUID, UUID> reply = cache.getReply().get();

        if (cache.getReply().get().containsKey(player.getUniqueId())) {

            OfflinePlayer target = Bukkit.getPlayer(reply.get(player.getUniqueId()));

            CommandSender targetsender = target.getPlayer();

            playersender.sendMessage(player, command.getString ("commands.msg-reply.player")
                    .replace("%player%", sender.getName())
                    .replace("%arg-1%", target.getName())
                    .replace("%message%", messages));

            playersender.sendMessage(targetsender, command.getString ("commands.msg-reply.player")
                    .replace("%player%", sender.getName())
                    .replace("%arg-1%", target.getName())
                    .replace("%message%", messages));

            reply.replace(reply.get(player.getUniqueId()), player.getUniqueId());


        } else {
            playersender.sendMessage(sender, lang.getString("error.no-reply"));

        }
        return true;
    }
}


