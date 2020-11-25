package code.commands;

import code.Manager;
import code.bukkitutils.SoundManager;
import code.modules.click.ChatMethod;
import code.modules.player.PlayerMessage;
import code.registry.ConfigManager;
import code.utils.Configuration;
import code.utils.PathManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BroadcastWorldCommand implements CommandClass {

    private final Manager manager;

    public BroadcastWorldCommand(Manager manager) {
        this.manager = manager;
    }

    @Command(names = {"broadcastworld", "bcw"})
    public boolean onCommand(@Sender Player player, @OptArg @Text String args) {

        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getManagingCenter().getSoundManager();
        PathManager pathManager = manager.getPathManager();

        ConfigManager files = manager.getFiles();

        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(pathManager.isCommandEnabled("broadcastworld"))) {
            pathManager.sendDisabledCmdMessage(player, "broadcastworld");
            return true;
        }

        UUID playeruuid = player.getUniqueId();

        if (!(player.hasPermission(config.getString("config.perms.broadcast-world")))){
            playersender.sendMessage(player, messages.getString("error.no-perms"));
            return true;
        }

        if (args.trim().isEmpty()) {
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            pathManager.getUsage(player, "broadcastworld", "<message>");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        ChatMethod chatMethod = manager.getPlayerMethods().getChatMethod();

        if (args.equalsIgnoreCase("-click")) {
            chatMethod.activateChat(playeruuid, true);
            return true;
        }

        String message = String.join(" ", args);

        for (Player playeronline : chatMethod.getWorldChat(player)) {
            playersender.sendMessage(playeronline, command.getString("commands.broadcast.text.world")
                    .replace("%world%", player.getWorld().getName())
                    .replace("%player%", player.getName())
                    .replace("%message%", message));
        }

        return true;
    }

}
