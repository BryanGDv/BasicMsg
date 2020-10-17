package code.registry;

import code.BasicMsg;
import code.commands.*;
import code.commands.modules.CustomI18n;
import code.Manager;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;

import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;


public class CommandRegistry implements LoaderService{

    private final Manager manager;
    private final BasicMsg plugin;

    private AnnotatedCommandTreeBuilder builder;
    private CommandManager commandManager;

    public CommandRegistry(BasicMsg plugin, Manager manager){
        this.plugin = plugin;
        this.manager = manager;
    }


    @Override
    public void setup() {

        manager.getLogs().log("Loading CommandRegistry");

        createCommandManager();

        commandManager.getTranslator().setProvider(new CustomI18n(manager));

        registerCommands(new BmsgCommand(plugin, manager));
        registerCommands(new IgnoreCommand(plugin, manager, manager.getCache()));
        registerCommands(new MsgCommand(plugin, manager, manager.getCache()));
        registerCommands(new ReplyCommand(plugin, manager, manager.getCache()));
        registerCommands(new SocialSpyCommand(plugin, manager));
        registerCommands(new UnIgnoreCommand(plugin, manager, manager.getCache()));

        manager.getLogs().log("Commands loaded!");
        plugin.getLogger().info("Commands loaded!");
    }

    public void registerCommands(CommandClass commandClass) {
        commandManager.registerCommands(builder.fromClass(commandClass));
    }

    private void createCommandManager() {
        commandManager = new BukkitCommandManager("BasicMsg");

        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        builder = new AnnotatedCommandTreeBuilderImpl(injector);
    }
}
