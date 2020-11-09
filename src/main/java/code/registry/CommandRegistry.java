package code.registry;

import code.BasicMsg;
import code.commands.*;
import code.commands.modules.CustomI18n;
import code.Manager;

import code.utils.Configuration;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;

import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

import java.util.List;


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

        registerCommands("bmsg", new BmsgCommand(plugin , manager));
        registerCommands("ignore", new IgnoreCommand(manager, manager.getCache()));
        registerCommands("msg", new MsgCommand(manager, manager.getCache()));
        registerCommands("reply", new ReplyCommand(manager, manager.getCache()));
        registerCommands("socialspy", new SocialSpyCommand(manager));
        registerCommands("unignore", new UnIgnoreCommand(manager, manager.getCache()));
        registerCommands("chat" , new ChatCommand(plugin, manager));

        manager.getLogs().log("Commands loaded!");
        plugin.getLogger().info("Commands loaded!");
    }

    public void registerCommands(String commandName, CommandClass commandClass) {
        if (manager.getPathManager().isCommandEnabled(commandName)) {
            commandManager.registerCommands(builder.fromClass(commandClass));
            manager.getLogs().log("Command: " + commandName + " loaded.");
        } else {
            manager.getLogs().log("Command: " + commandName + " unloaded.", 0);
        }
    }

    private void createCommandManager() {
        commandManager = new BukkitCommandManager("BasicMsg");

        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        builder = new AnnotatedCommandTreeBuilderImpl(injector);
    }
}
