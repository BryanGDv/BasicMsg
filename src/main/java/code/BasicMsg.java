package code;

import code.utils.UpdateCheck;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BasicMsg extends JavaPlugin {

    private Manager basicMsg;

    @Override
    public void onEnable() {

        registerManaging();
        registerPlaceholders();

        getLogger().info("Plugin created by zProgram.");
        getLogger().info("You are using version " + getDescription().getVersion() + ".");
        basicMsg.getLogs().log("- Plugin successfull loaded.", 2);

    }
    public void onDisable() {
        getLogger().info("Thx for using this plugin <3.");
        getDisableMessage();
    }
    public void registerManaging() {

        basicMsg = new Manager(this);

        if (basicMsg.getFiles().getConfig().getBoolean("config.metrics")) {
            Metrics metrics = new Metrics(this, 9139);
        }
        if (basicMsg.getFiles().getConfig().getBoolean("config.update-check")){
            getUpdateChecker();
        }

    }
    public void getUpdateChecker(){
        getLogger().info("Checking updating checker..");
        UpdateCheck
                .of(this)
                .resourceId(84926)
                .handleResponse((versionResponse, version) -> {
                    switch (versionResponse) {
                        case FOUND_NEW:
                            getLogger().info( "A new version of the plugin was found: " + version);
                            basicMsg.getLogs().log("A new version of the plugin was found: " + version);
                            break;
                        case LATEST:
                            getLogger().info( "You are on the latest version of the plugin.");
                            basicMsg.getLogs().log("You are on the latest version of the plugin.");
                            break;
                        case UNAVAILABLE:
                            getLogger().info("Unable to perform an update check.");
                            basicMsg.getLogs().log("A new version of the plugin was found: " + version);
                    }
                }).check();
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

