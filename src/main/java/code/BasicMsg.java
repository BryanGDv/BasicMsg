package code;

import code.api.BasicAPI;
import code.utils.UpdateCheck;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BasicMsg extends JavaPlugin {

    private Manager basicMsg;
    private BasicAPI api;

    @Override
    public void onEnable() {


        registerManaging();
        registerPlaceholders();

        getLogger().info("Plugin created by zProgram.");
        getLogger().info("You are using version " + getDescription().getVersion() + ".");
        getLogger().info("If you want support, you can join in: https://discord.gg/wQThjXs");

        basicMsg.getLogs().log("- Plugin successfull loaded.", 2);

    }
    public void getAPIS(){
        Bukkit.getLogger().info("test");
    }

    public BasicAPI getAPI(){
        return api;
    }

    public void onDisable() {
        getLogger().info("Thx for using this plugin <3.");
        getDisableMessage();
    }

    public void registerManaging() {

        api = new BasicAPI(basicMsg);
        basicMsg.getLogs().log("Loaded API.");
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

