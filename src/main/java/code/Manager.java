package code;

import code.commands.cache.CacheManager;
import code.debug.ErrorManager;
import code.interfaces.Services;
import code.modules.MethodManager;
import code.registry.CommandRegistry;
import code.registry.ConfigManager;
import code.registry.EventManager;
import code.utils.VariableManager;


public class Manager {

    private final BasicMsg plugin;

    private final VariableManager variables;
    private final ErrorManager debug;
    private final MethodManager methodManager;

    private final CommandRegistry commandRegistry;
    private final EventManager eventManager;
    private final ConfigManager configManager;

    private final CacheManager cache;


    public Manager(BasicMsg plugin){
        this.plugin = plugin;

        this.debug = new ErrorManager(plugin);
        debug.log("Loading plugin..");

        this.cache = new CacheManager(this);

        this.configManager = new ConfigManager(plugin, debug);
        configManager.setup();

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

}