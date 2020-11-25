package code.registry;

import code.BasicMsg;
import code.debug.DebugLogger;
import code.events.*;
import code.Manager;
import code.modules.click.ChatClickMethod;
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
        DebugLogger debug = manager.getLogs();

        PluginManager pl = Bukkit.getServer().getPluginManager();

        pl.registerEvents(new QuitListener(manager), plugin);
        debug.log("QuitListener loaded!");
        pl.registerEvents(new ChatListener(manager), plugin);
        debug.log("ChatListener loaded!");
        pl.registerEvents(new JoinListener(manager), plugin);
        debug.log("JoinListener loaded!");
        pl.registerEvents(new ChatFormat(manager), plugin);
        debug.log("ChatFormat loaded!");
        pl.registerEvents(new ChatClickMethod(manager), plugin);
        debug.log("ChatClickMethod loaded!");
        pl.registerEvents(new GuiListener(manager), plugin);
        debug.log("GuiListener loaded!");

        plugin.getLogger().info("Events loaded!");
    }
}
