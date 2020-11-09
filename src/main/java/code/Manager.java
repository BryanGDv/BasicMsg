package code;

import code.bukkitutils.WorldManager;
import code.debug.DebugLogger;
import code.modules.MethodManager;
import code.registry.CommandRegistry;
import code.registry.ConfigManager;
import code.registry.EventManager;
import code.revisor.RevisorManager;
import code.bukkitutils.SoundManager;
import code.utils.PathManager;
import code.utils.VariableManager;

import java.nio.file.Path;


public class Manager {

    private final BasicMsg plugin;

    private VariableManager variables;
    private MethodManager methodManager;

    private DebugLogger debug;
    private PathManager pathmanager;

    private CommandRegistry commandRegistry;
    private EventManager eventManager;
    private ConfigManager configManager;

    private RevisorManager revisorManager;

    private SoundManager soundManager;
    private WorldManager worldManager;

    private CacheManager cache;


    public Manager(BasicMsg plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup(){

        debug = new DebugLogger(plugin);

        cache = new CacheManager(this);

        configManager = new ConfigManager(plugin, this);
        configManager.setup();

        pathmanager = new PathManager(this);

        worldManager = new WorldManager(this);

        soundManager = new SoundManager(this);
        soundManager.setup();

        variables = new VariableManager(configManager, this);

        methodManager = new MethodManager(this);
        methodManager.setup();

        commandRegistry = new CommandRegistry(plugin, this);
        commandRegistry.setup();

        eventManager = new EventManager(plugin, this);
        eventManager.setup();

        revisorManager = new RevisorManager(this);

    }
    public WorldManager getWorldManager(){
        return worldManager;
    }
    public RevisorManager getRevisorManager() {
        return revisorManager;
    }

    public PathManager getPathManager(){
        return pathmanager;
    }
    public DebugLogger getLogs(){
        return debug;
    }

    public VariableManager getVariables(){
        return variables;
    }

    public CacheManager getCache(){
        return cache;
    }

    public MethodManager getPlayerMethods(){
        return methodManager;
    }

    public ConfigManager getFiles(){
        return configManager;
    }

    public SoundManager getSounds(){
        return soundManager;
    }
    public BasicMsg getPlugin(){
        return plugin;
    }

}