package code.commands;

import code.Manager;
import code.bukkitutils.SoundManager;
import code.cache.UserData;
import code.methods.commands.HelpOpMethod;
import code.methods.player.PlayerMessage;
import code.registry.ConfigManager;
import code.utils.Configuration;
import code.utils.module.ModuleCheck;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HelpopCommand implements CommandClass{

    private Manager manager;

    public HelpopCommand(Manager manager){
        this.manager = manager;
    }

    @Command(names =  {"helpop", "ac"})
    public boolean onCommand(@Sender Player player, @OptArg @Text String args){

        ConfigManager files = manager.getFiles();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        HelpOpMethod helpOpMethod = manager.getPlayerMethods().getHelpOpMethod();

        SoundManager sound = manager.getManagingCenter().getSoundManager();
        ModuleCheck moduleCheck = manager.getPathManager();

        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();


        if (!(moduleCheck.isCommandEnabled("helpop"))) {
            moduleCheck.sendDisableMessage(player, "helpop");
            return true;
        }


        UUID playeruuid = player.getUniqueId();

        if (args.trim().isEmpty()) {
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "helpop", "<message>");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (args.equalsIgnoreCase("-list")){

            List<String> helpopList = new ArrayList<>();

            for (Player playeronline : Bukkit.getServer().getOnlinePlayers()) {
                UserData onlineCache = manager.getCache().getPlayerUUID().get(playeronline.getUniqueId());
                if (playeronline.hasPermission(config.getString("config.perms.helpop-watch")) && onlineCache.isPlayerHelpOp()){
                    helpopList.add(playeronline.getName());
                }
            }
            playersender.sendMessage(player, command.getString("commands.helpop.space"));

            if (helpopList.isEmpty()) {
                playersender.sendMessage(player, messages.getString("error.helpop.anybody"));
            }else{
                playersender.sendMessage(player, command.getString("commands.helpop.list-helpop"));
                for (String helpopPlayers : helpopList) {
                    playersender.sendMessage(player, "&8- &f" + helpopPlayers);
                }
            }
            playersender.sendMessage(player, command.getString("commands.helpop.space"));
            sound.setSound(playeruuid, "sounds.on-helpop.list");
            return true;
        }

        UserData playerCache = manager.getCache().getPlayerUUID().get(player.getUniqueId());

        if (args.equalsIgnoreCase("-on")){
            if (!(player.hasPermission(config.getString("config.perms.helpop-watch")))){
                playersender.sendMessage(player, messages.getString("error.no-perms"));
                return true;
            }
            if (playerCache.isPlayerHelpOp()){
                playersender.sendMessage(player, messages.getString("error.helpop.activated"));
                return true;
            }

            helpOpMethod.set(playeruuid);
            playersender.sendMessage(player, command.getString("commands.helpop.player.enabled"));
            sound.setSound(playeruuid, "sounds.on-helpop.enable");
            return true;
        }

        if (args.equalsIgnoreCase("-off")){
            if (!(player.hasPermission(config.getString("config.perms.helpop-watch")))){
                playersender.sendMessage(player, messages.getString("error.no-perms"));
                return true;
            }

            if (!(playerCache.isPlayerHelpOp())){
                playersender.sendMessage(player, messages.getString("error.helpop.unactivated"));
                return true;
            }

            helpOpMethod.unset(playeruuid);
            playersender.sendMessage(player, command.getString("commands.helpop.player.disabled"));
            sound.setSound(playeruuid, "sounds.on-helpop.disable");
            return true;
        }

        if (args.equalsIgnoreCase("-toggle")){
            if (!(player.hasPermission(config.getString("config.perms.helpop-watch")))){
                playersender.sendMessage(player, messages.getString("error.no-perms"));
                return true;
            }

            helpOpMethod.toggle(playeruuid);
            playersender.sendMessage(player, command.getString("commands.helpop.player.toggle")
                        .replace("%mode%", helpOpMethod.getStatus()));
            sound.setSound(playeruuid, "sounds.on-helpop.toggle");
            return true;
        }


        playersender.sendMessage(player, command.getString("commands.helpop.received")
                .replace("%player%", player.getName()));

        for (Player playeronline : Bukkit.getServer().getOnlinePlayers()) {
            UserData onlineCache = manager.getCache().getPlayerUUID().get(playeronline.getUniqueId());

            if (playeronline.hasPermission(config.getString("config.perms.helpop-watch")) && onlineCache.isPlayerHelpOp()) {
                playersender.sendMessage(playeronline.getPlayer(), command.getString("commands.helpop.message")
                        .replace("%player%", player.getName())
                        .replace("%message%", String.join(" ", args)));
                sound.setSound(playeruuid, "sounds.on-helpop.receive-msg");
            }
        }
        return true;
    }
}
