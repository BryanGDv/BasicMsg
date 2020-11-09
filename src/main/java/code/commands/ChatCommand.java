package code.commands;

import code.BasicMsg;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.registry.ConfigManager;
import code.bukkitutils.SoundManager;
import code.utils.Configuration;
import code.utils.VariableManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatCommand implements CommandClass{

    private final Manager manager;
    private final BasicMsg plugin;

    public ChatCommand(BasicMsg plugin, Manager manager){
        this.manager = manager;
        this.plugin = plugin;
    }

    @Command(names = {"chat"})
    public boolean help(CommandSender sender, @OptArg String args) {

        ConfigManager files = manager.getFiles();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        SoundManager sound = manager.getSounds();
        VariableManager variable = manager.getVariables();

        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();
        Configuration utils = files.getBasicUtils();

        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;

        if (!(manager.getPathManager().isCommandEnabled("chat"))){
            playersender.sendMessage(sender, messages.getString("error.command-disabled")
                    .replace("%player%", player.getName())
                    .replace("%command%", "chat"));
            return true;
        }

        if (!(utils.getBoolean("utils.chat.enabled"))){
            playersender.sendMessage(sender, messages.getString("error.option-disabled")
                        .replace("%player%", player.getName())
                        .replace("%option%", "ChatManagement"));
            return true;
        }

        UUID playeruuid = player.getUniqueId();

        if (args == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/chat [help/reload]");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (args.equalsIgnoreCase("help")){
            variable.loopString(sender, command, "commands.chat.help");
            return true;

        }else if (args.equalsIgnoreCase("reload")){
            if (!(sender.hasPermission(config.getString("config.perms.chat-reload")))) {
                playersender.sendMessage(sender, messages.getString("error.no-perms"));
                return true;

            }
            playersender.sendMessage(sender, command.getString("commands.bmsg.load"));
            this.getReloadEvent(sender);
            return true;

        }else{
            playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload/sounds]");
            sound.setSound(player.getUniqueId(), "sounds.error");
        }

        return true;
    }
    public void getReloadEvent(CommandSender sender){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                PlayerMessage playersender = manager.getPlayerMethods().getSender();

                ConfigManager files = manager.getFiles();
                files.getBasicUtils().reload();
                playersender.sendMessage(sender, files.getCommand().getString("commands.bmsg.reload"));
            }

        },20L * 3);
    }
}
