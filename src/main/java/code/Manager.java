package code;

import code.debug.ErrorManager;
import code.modules.MethodManager;
import code.registry.CommandRegistry;
import code.registry.ConfigManager;
import code.registry.EventManager;
import code.sounds.SoundManager;
import code.utils.VariableManager;


public class Manager {

    private final BasicMsg plugin;

    private VariableManager variables;
    private ErrorManager debug;
    private MethodManager methodManager;

    private CommandRegistry commandRegistry;
    private EventManager eventManager;
    private ConfigManager configManager;

    public String test;

    private SoundManager soundManager;

    private CacheManager cache;


    public Manager(BasicMsg plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup(){

        debug = new ErrorManager(plugin);

        cache = new CacheManager(this);

        configManager = new ConfigManager(plugin, this);
        configManager.setup();

        soundManager = new SoundManager(this);
        soundManager.setup();

        variables = new VariableManager(configManager, this);

        methodManager = new MethodManager(this);
        methodManager.setup();

        commandRegistry = new CommandRegistry(plugin, this);
        commandRegistry.setup();

        eventManager = new EventManager(plugin, this);
        eventManager.setup();

    }

    public ErrorManager getLogs(){
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