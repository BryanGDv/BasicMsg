package code.registry;

import code.BasicMsg;
import code.utils.Configuration;
import code.debug.ErrorManager;

public class ConfigManager {

    // Tranquilos, esta clase no pesa mucho, pero no confunde a nadie :xd:.

    private Configuration config;
    private Configuration command;
    private Configuration players;
    private Configuration messages;

    private final ErrorManager debug;
    private final BasicMsg plugin;

    public ConfigManager(BasicMsg plugin, ErrorManager debug) {
        this.plugin = plugin;
        this.debug = debug;
    }

    public void setup() {
        config = new Configuration(plugin, "config.yml");
        debug.log("config.yml loaded!");
        command = new Configuration(plugin, "commands.yml");
        debug.log("command.yml loaded!");
        players = new Configuration(plugin, "players.yml");
        debug.log("players.yml loaded!");
        messages = new Configuration(plugin, "messages.yml");
        debug.log("messages.yml loaded!");

        plugin.getLogger().info("Config loaded!");
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getCommand() {
        return command;
    }

    public Configuration getPlayers() {
        return players;
    }

    public Configuration getMessages(){
        return messages;
    }
}

