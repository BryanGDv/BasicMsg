package code.commands;

import code.CacheManager;
import code.modules.PlayerSoundMethod;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;
import code.utils.VariableManager;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BmsgCommand implements CommandClass {

    private final BasicMsg plugin;
    private final Manager manager;

    public BmsgCommand(BasicMsg plugin, Manager manager){
        this.plugin = plugin;
        this.manager = manager;
    }

    @Command(names = {"bmsg", "bm"})
    public boolean help(CommandSender sender, @OptArg String arg1, @OptArg String arg2, @OptArg String arg3) {

        ConfigManager files = manager.getFiles();
        VariableManager variable = manager.getVariables();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        PlayerSoundMethod playersound = manager.getPlayerMethods().getPlayerSoundMethod();

        SoundManager sound = manager.getSounds();
        CacheManager cache = manager.getCache();

        Configuration soundfile = files.getSounds();
        Configuration utils = files.getBasicUtils();
        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();


        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;
        if (!(manager.getPathManager().isCommandEnabled("bmsg"))){
            playersender.sendMessage(sender, messages.getString("error.command-disabled")
                    .replace("%player%", player.getName())
                    .replace("%command%", "bmsg"));
            return true;
        }

        if (arg1 == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload/sounds]");
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }
        if (arg1.equalsIgnoreCase("help")) {
            variable.loopString(sender, command, "commands.bmsg.help");
            return true;

        }if (arg1.equalsIgnoreCase("reload")) {
            if (!(sender.hasPermission(config.getString("config.perms.reload")))) {
                playersender.sendMessage(sender, messages.getString("error.no-perms"));
                return true;
            }
            if (arg2 == null) {
                playersender.sendMessage(sender, messages.getString("error.no-arg"));
                playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg reload [file/all]");
                sound.setSound(player.getUniqueId(), "sounds.error");
                return true;

            }if (arg2.equalsIgnoreCase("all")) {
                playersender.sendMessage(sender, command.getString("commands.bmsg.load"));
                this.getReloadEvent(sender, "all");

            } else {
                playersender.sendMessage(sender, command.getString("commands.bmsg.load-file"));
                this.getReloadEvent(sender, arg2);

                return true;
            }
            return true;

        }if (arg1.equalsIgnoreCase("support")) {
            if (manager.getFiles().getConfig().getBoolean("config.allow-support")) {
                playersender.sendMessage(sender, "&b[Server] &8| &fIf you want support of the plugin:");
                playersender.sendMessage(sender, "&8- &fJoin: &ahttps://discord.gg/wQThjXs");

            } else {
                playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
                playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload]");
                sound.setSound(player.getUniqueId(), "sounds.error");
            }
            return true;

        } if (arg1.equalsIgnoreCase("sounds")) {
            Set<UUID> sounds = cache.getPlayerSounds();
            if (!(soundfile.getBoolean("sounds.enabled-all"))) {
                playersender.sendMessage(sender, messages.getString("error.no-sound"));
                return true;
            }
            if (!(sounds.contains(player.getUniqueId()))) {
                playersound.set(player.getUniqueId());
                playersender.sendMessage(sender, command.getString("commands.bmsg.sounds.enabled"));
                sound.setSound(player.getUniqueId(), "sounds.enable-mode");
            } else {
                playersound.unset(player.getUniqueId());
                playersender.sendMessage(sender, command.getString("commands.bmsg.sounds.disabled"));
            }

        } if (arg1.equalsIgnoreCase("debug")){
            if (player.hasPermission(config.getString("config.perms.debug"))){
                playersender.sendMessage(sender, messages.getString("error.no-perms"));
                return true;
            }

            if (arg2 == null){
                playersender.sendMessage(sender, messages.getString("error.no-arg"));
                playersender.sendMessage(sender, "&8- &f/bmsg debug [pwc]");
                sound.setSound(player.getUniqueId(), "sounds.error");
                return true;
            }

            if (arg2.equalsIgnoreCase("pwc")){
                Set<String> worldlist = utils.getConfigurationSection("utils.chat.per-world-chat.worlds").getKeys(true);

                if (arg3 == null){
                    playersender.sendMessage(sender, messages.getString("error.no-arg"));
                    playersender.sendMessage(sender, "&8- &fWorlds: "+ String.join(" ", worldlist));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;

                }if (arg3.equalsIgnoreCase("-all")){
                    playersender.sendMessage(sender, messages.getString("commands.bmsg.debug.list-worlds"));
                    for (String worldname : worldlist) {
                        playersender.sendMessage(sender, "&8- &f " + worldname);
                    }
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                List<String> worldname = utils.getStringList("utils.chat.per-world-chat.worlds." + arg3);
                if (worldname == null){
                    playersender.sendMessage(sender, command.getString("error.debug.unknown-world"
                            .replace("%world%", arg3)));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                playersender.sendMessage(sender, command.getString("commands.bmsg.debug.worldpath-info").replace("%world%", arg3));
                for (String worldnamelist : worldname) {
                    playersender.sendMessage(sender, "&8- &f" + worldnamelist);
                }
                return true;
            }else{
                playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
                playersender.sendMessage(sender, "&8- &f/bmsg debug [pwc]");
                sound.setSound(player.getUniqueId(), "sounds.error");
            }
        } else {
            playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload/sounds]");
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
                SoundManager sound = manager.getSounds();

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

}
