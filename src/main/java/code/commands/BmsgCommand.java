package code.commands;

import code.cache.UserCache;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;

import code.utils.ListManager;
import code.utils.PathManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import code.BasicMsg;
import code.utils.Configuration;
import code.utils.VariableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BmsgCommand implements CommandClass {

    private final BasicMsg plugin;
    private final Manager manager;

    public BmsgCommand(BasicMsg plugin, Manager manager){
        this.plugin = plugin;
        this.manager = manager;
    }

    @Command(names = {"bmsg", "bm"})
    public boolean help(@Sender Player player, @OptArg String arg1, @OptArg String arg2, @OptArg String arg3) {

        ConfigManager files = manager.getFiles();
        VariableManager variable = manager.getVariables();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getManagingCenter().getSoundManager();
        PathManager pathManager = manager.getPathManager();

        Configuration soundfile = files.getSounds();
        Configuration utils = files.getBasicUtils();
        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();


        if (!(pathManager.isCommandEnabled("bmsg"))) {
            pathManager.sendDisabledCmdMessage(player, "bmsg");
            return true;
        }

        if (arg1 == null) {
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            pathManager.getUsage(player, "bmsg", "help, reload, commands, support, sounds, debug, restore");
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }
        if (arg1.equalsIgnoreCase("help")){
            variable.loopString(player, command, "commands.bmsg.help.pages");
            return true;
        }
        if (arg1.equalsIgnoreCase("commands")) {
            if (arg2 == null) {
                playersender.sendMessage(player, command.getString("commands.bmsg.commands.format")
                        .replace("%page%", "1")
                        .replace("%max_page%", String.valueOf(getMaxPage())));
                variable.loopString(player, command, "commands.bmsg.commands.pages.1");
                return true;
            }

            if (!(getHelp().contains(arg2))){
                playersender.sendMessage(player, messages.getString("error.unknown-page"));
                return true;
            }


            playersender.sendMessage(player, command.getString("commands.bmsg.commands.format")
                    .replace("%page%", arg2)
                    .replace("%max_page%", String.valueOf(getMaxPage())));
            variable.loopString(player, command, "commands.bmsg.commands.pages." + arg2);
            return true;

        }if (arg1.equalsIgnoreCase("reload")) {
            if (!(player.hasPermission(config.getString("config.perms.reload")))) {
                playersender.sendMessage(player, messages.getString("error.no-perms"));
                return true;
            }
            if (arg2 == null) {
                playersender.sendMessage(player, messages.getString("error.no-arg"));
                pathManager.getUsage(player, "bmsg", arg1, "all, <file>");
                sound.setSound(player.getUniqueId(), "sounds.error");
                return true;

            }if (arg2.equalsIgnoreCase("all")) {
                playersender.sendMessage(player, command.getString("commands.bmsg.load"));
                this.getReloadEvent(player, "all");

            } else {
                playersender.sendMessage(player, command.getString("commands.bmsg.load-file"));
                this.getReloadEvent(player, arg2);

                return true;
            }
            return true;

        }if (arg1.equalsIgnoreCase("support")) {
            if (manager.getFiles().getConfig().getBoolean("config.allow-support")) {
                playersender.sendMessage(player, "&b[Server] &8| &fIf you want support of the plugin:");
                playersender.sendMessage(player, "&8- &fJoin: &ahttps://discord.gg/wQThjXs");

            } else {
                playersender.sendMessage(player, messages.getString("error.unknown-arg"));
                pathManager.getUsage(player, "bmsg", "help, reload, commands, support, sounds, debug, restore");
                sound.setSound(player.getUniqueId(), "sounds.error");
            }
            return true;

        } if (arg1.equalsIgnoreCase("sounds")) {
            UserCache playerSound = manager.getCache().getPlayerUUID().get(player.getUniqueId());
            if (!(soundfile.getBoolean("sounds.enabled-all"))) {
                playersender.sendMessage(player, messages.getString("error.no-sound"));
                return true;
            }
            if (playerSound.isPlayersoundMode()){
                playerSound.setPlayersoundMode(true);
                playersender.sendMessage(player, command.getString("commands.bmsg.sounds.enabled"));
                sound.setSound(player.getUniqueId(), "sounds.enable-mode");
            } else {
                playerSound.setPlayersoundMode(false);
                playersender.sendMessage(player, command.getString("commands.bmsg.sounds.disabled"));
            }

        } if (arg1.equalsIgnoreCase("debug")){
            if (!(player.hasPermission(config.getString("config.perms.debug")))){
                playersender.sendMessage(player, messages.getString("error.no-perms"));
                return true;
            }

            if (arg2 == null){
                playersender.sendMessage(player, messages.getString("error.no-arg"));
                pathManager.getUsage(player, "bmsg", arg1, "<group-pwc>");
                sound.setSound(player.getUniqueId(), "sounds.error");
                return true;
            }

            if (arg2.equalsIgnoreCase("pwc")) {
                Set<String> worldlist = utils.getConfigurationSection("utils.chat.per-world-chat.worlds").getKeys(true);

                if (arg3 == null) {
                    playersender.sendMessage(player, messages.getString("error.no-arg"));
                    playersender.sendMessage(player, "&8- &fWorlds: " + String.join(" ", worldlist));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                if (arg3.equalsIgnoreCase("-all")) {
                    playersender.sendMessage(player, command.getString("commands.bmsg.debug.list.worlds"));
                    for (String worldname : worldlist) {
                        playersender.sendMessage(player, "&8- &f " + worldname);
                    }
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                List<String> worldname = utils.getStringList("utils.chat.per-world-chat.worlds." + arg3);
                if (worldname == null) {
                    playersender.sendMessage(player, command.getString("error.debug.unknown-world"
                            .replace("%world%", arg3)));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                playersender.sendMessage(player, command.getString("commands.bmsg.debug.worldpath-info").replace("%world%", arg3));
                for (String worldnamelist : worldname) {
                    playersender.sendMessage(player, "&8- &f" + worldnamelist);
                }
                return true;
            } if (arg2.equalsIgnoreCase("commands")) {
                playersender.sendMessage(player, command.getString("commands.bmsg.debug.list.commands"));
                for (String commandName : manager.getListManager().getCommands()){
                    if (manager.getPathManager().isCommandEnabled(commandName)){
                        playersender.sendMessage(player, "&8- &f" + commandName + " &a[Enabled]");
                    }else{
                        playersender.sendMessage(player, "&8- &f" + commandName + " &c[Disabled]");
                    }
                }
                return true;
            } if (arg2.equalsIgnoreCase("modules")){
                playersender.sendMessage(player, command.getString("commands.bmsg.debug.list.modules"));
                for (String moduleName : manager.getListManager().getModules()){
                    if (manager.getPathManager().isOptionEnabled(moduleName)){
                        playersender.sendMessage(player, "&8- &f" + moduleName + " &a[Enabled]");
                    }else{
                        playersender.sendMessage(player, "&8- &f" + moduleName + " &c[Disabled]");
                    }
                }

            }else{
                playersender.sendMessage(player, messages.getString("error.unknown-arg"));
                pathManager.getUsage(player, "bmsg", arg1, "commands, modules");
                sound.setSound(player.getUniqueId(), "sounds.error");
                return true;
            }
        }if (arg1.equalsIgnoreCase("restore")){
            if (arg2 == null){
                playersender.sendMessage(player, messages.getString("error.no-arg"));
                pathManager.getUsage(player, "bmsg", arg1, "commands, modules");
                sound.setSound(player.getUniqueId(), "sounds.error");
                return true;
            }
            ListManager listManager = manager.getListManager();
            if (arg2.equalsIgnoreCase("commands")){
                config.set("config.modules.enabled-commands", listManager.getCommands());
                config.save();
                playersender.sendMessage(player, command.getString("commands.bmsg.restore.commands"));
                return true;

            }if (arg2.equalsIgnoreCase("modules")){
                config.set("config.modules.enabled-options", listManager.getModules());
                config.save();
                playersender.sendMessage(player, command.getString("commands.bmsg.restore.commands"));
                return true;

            }else{
                playersender.sendMessage(player, messages.getString("error.unknown-arg"));
                pathManager.getUsage(player, "bmsg", arg1, "commands, modules");
                sound.setSound(player.getUniqueId(), "sounds.error");
            }
            return true;
        }else{
            playersender.sendMessage(player, messages.getString("error.unknown-arg"));
            pathManager.getUsage(player, "bmsg", "help, reload, commands, support, sounds, debug, restore");
            sound.setSound(player.getUniqueId(), "sounds.error");
        }
        return true;
    }

    public void getReloadEvent(CommandSender sender, String string){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {

                PlayerMessage playersender = manager.getPlayerMethods().getSender();
                Player player = (Player) sender;

                ConfigManager files = manager.getFiles();
                SoundManager sound = manager.getManagingCenter().getSoundManager();

                Map<String, Configuration> fileMap = manager.getCache().getConfigFiles();

                if (fileMap.get(string) == null){
                    if (string.equalsIgnoreCase("all")) {
                        for (Configuration config : fileMap.values()){
                            config.reload();
                        }
                        playersender.sendMessage(sender, files.getCommand().getString("commands.bmsg.reload"));
                        return;
                    }
                    playersender.sendMessage(sender, files.getMessages().getString("error.unknown-arg"));
                    playersender.sendMessage(sender, "&8- &fFiles: &a[commands, config, messages, players, sounds, utils]");
                    sound.setSound(player.getUniqueId(), "sounds.error");
                }else{
                    fileMap.get(string).reload();
                    playersender.sendMessage(sender, files.getCommand().getString("commands.bmsg.reload-file").replace("%file%", StringUtils.capitalize(string)));
                    sound.setSound(player.getUniqueId(), "sounds.on-reload");

                }

            }
        }, 20L * 3);
    }

    public Integer getMaxPage() {
        List<String> maxpages = new ArrayList<>(getHelp());

        return maxpages.size();
    }

    public Set<String> getHelp(){
         Configuration commands = manager.getFiles().getCommand();
         return commands.getConfigurationSection("commands.bmsg.commands.pages").getKeys(true);
    }
}
