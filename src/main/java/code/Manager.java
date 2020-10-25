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

    private final VariableManager variables;
    private final ErrorManager debug;
    private final MethodManager methodManager;

    private final CommandRegistry commandRegistry;
    private final EventManager eventManager;
    private final ConfigManager configManager;

    public String test;

    private final SoundManager soundManager;

    private final CacheManager cache;


    public Manager(BasicMsg plugin){
        this.plugin = plugin;

        this.debug = new ErrorManager(plugin);
        debug.log("Loading plugin..");
        this.cache = new CacheManager(this);

        this.configManager = new ConfigManager(plugin, debug);
        configManager.setup();

        this.soundManager = new SoundManager(this);
        soundManager.setup();

        this.variables = new VariableManager(configManager, this);

        this.methodManager = new MethodManager(this);
        methodManager.setup();

        this.commandRegistry = new CommandRegistry(plugin, this);
        commandRegistry.setup();

        this.eventManager = new EventManager(plugin, this);
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