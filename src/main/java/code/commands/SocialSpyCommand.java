package code.commands;

import code.modules.SocialSpyMethod;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.PlayerMessage;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;


public class SocialSpyCommand implements CommandClass {

    private final BasicMsg plugin;
    private final Manager manager;

    public SocialSpyCommand(BasicMsg plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;

    }

    @Command(names = "socialspy")
    public boolean onCommand(CommandSender sender, @OptArg String args, @OptArg OfflinePlayer target) {

        SocialSpyMethod socialspy = manager.getPlayerMethods().getSocialSpyMethod();
        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration config = files.getConfig();
        Configuration messages = files.getMessages();
        Configuration command = files.getCommand();


        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;

        if (!(sender.hasPermission(config.getString("config.perms.socialspy")))) {
            playersender.sendMessage(sender, messages.getString("error.no-perms"));
            return true;

        }

        if (args == null) {
            socialspy.toggle(player.getUniqueId());
            playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.toggle").replace("%mode%", socialspy.getStatus()));
            return true;
        }


        if (args.equalsIgnoreCase("on")) {
            if (target == null) {

                if (socialspy.getVariable() == null){
                    socialspy.set(player.getUniqueId());
                    playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.enabled"));
                    return true;
                }
                if (socialspy.getVariable().contains(player.getUniqueId())) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.activated"));
                    return true;
                }
                socialspy.set(player.getUniqueId());
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.enabled"));
            } else {
                String targetname = target.getName();
                if (!target.isOnline()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.player-offline"));
                    return true;
                }
                if (socialspy.getVariable().contains(target.getUniqueId())) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.arg-2-activated").replace("%arg-2%", targetname));
                    return true;
                }
                socialspy.set(target.getUniqueId());
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.arg-2.enabled").replace("%arg-2%", target.getName()));
                playersender.sendMessage(target.getPlayer(), command.getString("commands.socialspy.player.enabled"));
            }
            return true;
        }

        if (args.equalsIgnoreCase("off")) {
            if (target == null){

                if (!(socialspy.getVariable().contains(player.getUniqueId()))) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.unactivated"));
                    return true;
                }
                socialspy.unset(player.getUniqueId());
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.disabled"));
            } else {
                String targetname = target.getName();

                if (!target.isOnline()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.player-offline"));
                    return true;
                }
                if (!(socialspy.getVariable().contains(target.getUniqueId()))) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.arg-2-unactivated").replace("%arg-2%", targetname));
                    return true;
                }
                socialspy.unset(target.getUniqueId());

                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.arg-2.disabled").replace("%arg-2%", targetname));
                playersender.sendMessage(target.getPlayer(), command.getString("commands.socialspy.player.disabled"));

            }
            return true;
        } else {
            playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/socialspy [on/off] [player]");


        }

        return true;
    }
}
