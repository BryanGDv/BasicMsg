package code.utils;

import code.Manager;
import code.modules.PlayerMessage;
import code.registry.ConfigManager;
import org.bukkit.command.CommandSender;


public class VariableManager {

    private final ConfigManager config;
    private final Manager manager;

    public VariableManager(ConfigManager config, Manager manager){
        this.config = config;
        this.manager = manager;
    }

    public void loopString(CommandSender sender, Configuration config, String string){
        PlayerMessage player = manager.getPlayerMethods().getSender();
        for (String msg : config.getStringList(string)){
            player.sendMessage(sender, msg);
        }
    }

    public String replaceString(String string){
        return string
                .replace(config.getConfig().getString("config.p-variable"), config.getConfig().getString("config.prefix"))
                .replace(config.getConfig().getString("config.e-variable"), config.getConfig().getString("config.error"));
    }
}
