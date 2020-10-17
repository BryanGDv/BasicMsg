package code;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BasicMsg extends JavaPlugin {

    private Manager basicMsg;


    public void onEnable() {

        registerManager();
        registerPlaceholders();

        getLogger().info("Plugin created by zProgram.");
        getLogger().info("You are using version " + getDescription().getVersion() + ".");
        basicMsg.getLogs().log("- Plugin successfull loaded.", 2);

    }
    public void onDisable() {
        getLogger().info("Thx for using this plugin <3.");
        getDisableMessage();
    }
    public void registerManager() {

        basicMsg = new Manager(this);

    }
    public void registerPlaceholders(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI hooked!");
            basicMsg.getLogs().log("PlaceholderAPI loaded!");
        }else{
            basicMsg.getLogs().log("PlaceholderAPI is not loaded !", 0);
        }
    }
    public void getDisableMessage(){
        int number = (int) (Math.random() * 2 + 1);
        if (number == 1) {
            getLogger().info("Goodbye!");
        } else if (number == 2) {
            getLogger().info("See you later!");
        } else {
            getLogger().info("You shouldn't watch this..");
        }
    }
}

