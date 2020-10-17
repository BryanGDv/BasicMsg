package code.commands;

import code.registry.ConfigManager;
import code.Manager;
import code.modules.PlayerMessage;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;
import code.utils.VariableManager;

public class BmsgCommand implements CommandClass {

    private final BasicMsg plugin;
    private final Manager manager;

    public BmsgCommand(BasicMsg plugin, Manager manager){
        this.plugin = plugin;
        this.manager = manager;
    }

    @Command(names = "bmsg")
    public boolean help(CommandSender sender, @OptArg String args) {

        ConfigManager files = manager.getFiles();
        VariableManager variable = manager.getVariables();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();


        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }
        if (args == null) {

            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload]");
            return true;
        }

        if (args.equalsIgnoreCase("help")) {
            variable.loopString(sender, command, "commands.bmsg.help");
            return true;

        }if (args.equalsIgnoreCase("reload")) {
            if (!(sender.hasPermission(config.getString("config.perms.reload")))){
                playersender.sendMessage(sender, messages.getString("error.no-perms"));
                return true;
            }
            playersender.sendMessage(sender, command.getString("commands.bmsg.load"));
            this.getReloadEvent(sender);

        } else {
            playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload]");
            return true;
        }
        return false;
    }

    public void getReloadEvent(CommandSender sender){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                manager.getPlayerMethods().getSender().sendMessage(sender, manager.getFiles().getCommand().getString("commands.bmsg.reload"));
            }
        }, 20L * 3);
    }

}
