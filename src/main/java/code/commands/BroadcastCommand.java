package code.commands;

import code.Manager;
import code.bukkitutils.SoundManager;
import code.methods.click.ChatMethod;
import code.methods.player.PlayerMessage;
import code.registry.ConfigManager;
import code.utils.Configuration;
import code.utils.module.ModuleCheck;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BroadcastCommand implements CommandClass {

    private final Manager manager;

    public BroadcastCommand(Manager manager){
        this.manager = manager;
    }


    @Command(names =  {"broadcast", "bc"})
    public boolean onCommand(@Sender Player player, @OptArg @Text String args){

        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        ModuleCheck moduleCheck = manager.getPathManager();
        SoundManager sound = manager.getManagingCenter().getSoundManager();

        ConfigManager files = manager.getFiles();

        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(moduleCheck.isCommandEnabled("broadcast"))) {
            moduleCheck.sendDisableMessage(player, "broadcast");
            return true;
        }

        UUID playeruuid = player.getUniqueId();

        if (!(player.hasPermission(config.getString("config.perms.broadcast")))){
            playersender.sendMessage(player, messages.getString("error.no-perms"));
            return true;
        }

        if (args.trim().isEmpty()){
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "broadcast", "<message>");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }


        ChatMethod chatMethod = manager.getPlayerMethods().getChatMethod();

        if (args.equalsIgnoreCase("-click")) {
            chatMethod.activateChat(playeruuid, false);
            return true;
        }

        String message = String.join(" ", args);

        for (Player playeronline : Bukkit.getServer().getOnlinePlayers()){
            playersender.sendMessage(playeronline, command.getString("commands.broadcast.text.global")
                    .replace("%player%", player.getName())
                    .replace("%message%", message));
        }

        return true;
    }

}
