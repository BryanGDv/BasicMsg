package code.commands;

import code.CacheManager;
import code.modules.PlayerSoundMethod;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.sounds.SoundManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.BasicMsg;
import code.utils.Configuration;
import code.utils.VariableManager;

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
    public boolean help(CommandSender sender, @OptArg String args) {

        ConfigManager files = manager.getFiles();
        VariableManager variable = manager.getVariables();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        PlayerSoundMethod playersound = manager.getPlayerMethods().getPlayerSoundMethod();

        SoundManager sound = manager.getSounds();
        CacheManager cache = manager.getCache();

        Configuration soundfile = files.getSounds();
        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();


        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;

        if (args == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload/sounds]");
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;
        }
        if (args.equalsIgnoreCase("help")) {
            variable.loopString(sender, command, "commands.bmsg.help");
            return true;

        }if (args.equalsIgnoreCase("reload")) {
            if (!(sender.hasPermission(config.getString("config.perms.reload")))) {
                playersender.sendMessage(sender, messages.getString("error.no-perms"));
                return true;
            }
            playersender.sendMessage(sender, command.getString("commands.bmsg.load"));
            this.getReloadEvent(sender);
            return true;

        }if (args.equalsIgnoreCase("support")) {
            if (manager.getFiles().getConfig().getBoolean("config.allow-support")) {
                playersender.sendMessage(sender, "&b[Server] &8| &fIf you want support of the plugin:");
                playersender.sendMessage(sender, "&8- &fJoin: &ahttps://discord.gg/wQThjXs");

            } else {
                playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
                playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload]");
                sound.setSound(player.getUniqueId(), "sounds.error");
            }
            return true;

        } if (args.equalsIgnoreCase("sounds")){
            Set<UUID> sounds = cache.getPlayerSounds();
            if (!(soundfile.getBoolean("sounds.enabled-all"))){
                playersender.sendMessage(sender, messages.getString("error.no-sound"));
                return true;
            }
            if (!(sounds.contains(player.getUniqueId()))){
                playersound.set(player.getUniqueId());
                playersender.sendMessage(sender, command.getString("commands.bmsg.sounds.enabled"));
                sound.setSound(player.getUniqueId(), "sounds.enable-mode");
            }else{
                playersound.unset(player.getUniqueId());
                playersender.sendMessage(sender, command.getString("commands.bmsg.sounds.disabled"));
            }
        } else {
            playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/bmsg [help/reload/sounds]");
            sound.setSound(player.getUniqueId(), "sounds.error");
        }
        return true;
    }

    public void getReloadEvent(CommandSender sender){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                ConfigManager files = manager.getFiles();
                files.getConfig().reload();
                files.getMessages().reload();
                files.getCommand().reload();
                files.getSounds().reload();
                files.getPlayers().reload();
                manager.getPlayerMethods().getSender().sendMessage(sender, manager.getFiles().getCommand().getString("commands.bmsg.reload"));
            }
        }, 20L * 3);
    }

}
