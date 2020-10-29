package code.registry;

import code.BasicMsg;
import code.CacheManager;
import code.Manager;
import code.utils.Configuration;
import code.debug.ErrorManager;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

import java.util.Map;

public class ConfigManager {

    // Tranquilos, esta clase no pesa mucho, pero no confunde a nadie :xd:.

    private Configuration config;
    private Configuration command;
    private Configuration players;
    private Configuration messages;
    private Configuration sounds;
    private Configuration utils;

    private final BasicMsg plugin;
    private final Manager manager;

    public ConfigManager(BasicMsg plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void setup() {
        Map<String, Configuration> configFiles = manager.getCache().getConfigFiles();

        config = this.setConfiguration("config.yml");
        configFiles.put("config", config);

        command = this.setConfiguration("commands.yml");
        configFiles.put("commands", command);

        players = this.setConfiguration("players.yml");
        configFiles.put("players", players);

        messages = this.setConfiguration("messages.yml");
        configFiles.put("messages", messages);

        sounds = this.setConfiguration("sounds.yml");
        configFiles.put("sounds", sounds);

        utils = this.setConfiguration("utils.yml");
        configFiles.put("utils", utils);
        plugin.getLogger().info("Config loaded!");
    }

    public Configuration setConfiguration(String string){
        ErrorManager log = manager.getLogs();
        log.log(string + " loaded!");
        return new Configuration(plugin, string);

    }
    public Configuration getConfig() {
        return config;
    }

    public Configuration getBasicUtils(){
        return utils;
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

    public Configuration getSounds(){
        return sounds;
    }
}

