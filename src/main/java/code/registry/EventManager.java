package code.registry;

import code.BasicMsg;
import code.debug.ErrorManager;
import code.events.ChatListener;
import code.events.QuitListener;
import code.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    private final BasicMsg plugin;
    private final Manager manager;

    public EventManager(BasicMsg plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }


    public void setup() {
        ErrorManager debug = manager.getLogs();

        PluginManager pl = Bukkit.getServer().getPluginManager();

        pl.registerEvents(new QuitListener(manager.getCache()), plugin);
        debug.log("QuitListener loaded!");
        pl.registerEvents(new ChatListener(manager.getFiles()), plugin);
        debug.log("ChatListener loaded!");

        plugin.getLogger().info("Events loaded!");
    }
}
