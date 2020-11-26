package code.utils.module;


import code.Manager;
import code.bukkitutils.SoundManager;
import code.methods.player.PlayerMessage;
import code.utils.Configuration;
import org.bukkit.entity.Player;

import java.util.List;

public class ModuleCheck {

    private Manager manager;
    private Configuration config;

    public ModuleCheck(Manager manager) {
        this.manager = manager;
        this.config = manager.getFiles().getConfig();
    }

    public boolean isCommandEnabled(String commandName) {
        List<String> commandFile = config.getStringList("config.modules.enabled-commands");
        for (String commandEnabledCmds : commandFile) {
            if (commandEnabledCmds.equalsIgnoreCase(commandName)) {
                return true;
            }
        }
        return false;
    }

    public void sendDisableMessage(Player player, String command){

        PlayerMessage sender = manager.getPlayerMethods().getSender();
        SoundManager sound = manager.getManagingCenter().getSoundManager();

        Configuration messages = manager.getFiles().getMessages();

        sender.sendMessage(player, messages.getString("error.command-disabled")
                .replace("%player%", player.getName())
                .replace("%command%", command));
        sender.sendMessage(player, "&e[!] &8| &fYou need to restart the server to activate o unactivate the command.");
        sound.setSound(player.getUniqueId(), "sounds.error");

    }

    public boolean isOptionEnabled(String optionName) {
        List<String> optionFile = config.getStringList("config.modules.enabled-options");
        for (String optionEnabledOptions : optionFile) {
            if (optionEnabledOptions.equalsIgnoreCase(optionName)) {
                return true;
            }
        }
        return false;
    }

    public void getUsage(Player player, String command, String... args){

        Configuration config = manager.getFiles().getConfig();

        if (!(config.getBoolean("config.allow-usage"))){
            return;
        }
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        StringBuilder message = new StringBuilder();
        for (String arg : args){
            if (!(arg.contains(","))){
                message.append(arg).append(" ");
            } else {
                message.append("[").append(arg).append("] ");
            }
        }

        playersender.sendMessage(player, "&8- &fUsage: &a/" + command + " " + message);

    }
}