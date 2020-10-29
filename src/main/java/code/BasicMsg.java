package code;

import code.api.BasicAPI;
import code.utils.UpdateCheck;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BasicMsg extends JavaPlugin {

    private Manager basicMsg;

    private static BasicAPI api;

    @Override
    public void onEnable() {


        registerManaging();
        registerPlaceholders();

        getLogger().info("Plugin created by "+getDescription().getAuthors() + "");
        getLogger().info("You are using version " + getDescription().getVersion() + ".");
        getLogger().info("If you want support, you can join in: https://discord.gg/wQThjXs");

        basicMsg.getLogs().log("- Plugin successfull loaded.", 2);

    }

    public static BasicAPI getAPI(){
        return api;
    }

    public void onDisable() {
        getLogger().info("Thx for using this plugin <3.");
        getDisableMessage();
    }

    public void registerManaging() {

        basicMsg = new Manager(this);
        basicMsg.getLogs().log("Loading API...");
        api = new BasicAPI(basicMsg);
        basicMsg.getLogs().log("Loaded.");

        if (basicMsg.getFiles().getConfig().getBoolean("config.metrics")) {
            Metrics metrics = new Metrics(this, 9139);
        }
        if (basicMsg.getFiles().getConfig().getBoolean("config.update-check")){
            getUpdateChecker();
        }

    }
    public Manager getManager(){
        return basicMsg;
    }
    public void getUpdateChecker(){
        getLogger().info("Checking updating checker..");
        UpdateCheck.init(this, 84926);
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

