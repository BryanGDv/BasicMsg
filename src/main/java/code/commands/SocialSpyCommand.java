package code.commands;

import code.CacheManager;
import code.cache.UserCache;
import code.modules.SocialSpyMethod;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import code.utils.PathManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.utils.Configuration;
import sun.misc.Cache;

import java.util.ArrayList;
import java.util.List;


public class SocialSpyCommand implements CommandClass{

    private final Manager manager;

    public SocialSpyCommand(Manager manager) {
        this.manager = manager;

    }

    @Command(names = {"socialspy", "spy"})
    public boolean onCommand(@Sender Player player, @OptArg String args, @OptArg OfflinePlayer target) {

        SocialSpyMethod socialspy = manager.getPlayerMethods().getSocialSpyMethod();
        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getManagingCenter().getSoundManager();
        PathManager pathManager = manager.getPathManager();

        Configuration config = files.getConfig();
        Configuration messages = files.getMessages();
        Configuration command = files.getCommand();

        if (!(pathManager.isCommandEnabled("socialspy"))) {
            pathManager.sendDisabledCmdMessage(player, "socialspy");
            return true;
        }


        if (!(player.hasPermission(config.getString("config.perms.socialspy")))) {
            playersender.sendMessage(player, messages.getString("error.no-perms"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;

        }

        if (args == null) {
            socialspy.toggle(player.getUniqueId());
            playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.toggle")
                    .replace("%mode%", socialspy.getStatus()));
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.toggle");
            return true;
        }

        UserCache playerSpy = manager.getCache().getPlayerUUID().get(player.getUniqueId());

        if (args.equalsIgnoreCase("list")) {
            playersender.sendMessage(player, command.getString("commands.socialspy.space"));

            List<String> socialspyList = new ArrayList<>();

            for (UserCache cache : manager.getCache().getPlayerUUID().values()) {
                if (cache.isSocialSpyMode()) {
                    socialspyList.add(cache.getPlayer().getName());
                }
            }

            if (socialspyList.isEmpty()) {
                playersender.sendMessage(player, messages.getString("error.socialspy.anybody"));

            } else {
                playersender.sendMessage(player, command.getString("commands.socialspy.list-spyplayers"));
                for (String playerSyping : socialspyList) {
                    playersender.sendMessage(player, "&8- &f" + playerSyping);
                }
            }

            playersender.sendMessage(player, command.getString("commands.socialspy.space"));
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.list");
            return true;
        }

        if (args.equalsIgnoreCase("on")) {
            if (target == null) {
                if (playerSpy.isSocialSpyMode()){
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.activated"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                playerSpy.toggleSocialSpy(true);
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.enabled"));
                Bukkit.getLogger().info(String.valueOf(playerSpy.isSocialSpyMode()));
            } else {

                if (!target.isOnline()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.player-offline"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                UserCache targetSpy = manager.getCache().getPlayerUUID().get(target.getUniqueId());
                String targetname = target.getName();

                if (targetSpy.isSocialSpyMode()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.arg-2-activated").replace("%arg-2%", targetname));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                targetSpy.toggleSocialSpy(true);
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.arg-2.enabled").replace("%arg-2%", target.getName()));
                playersender.sendMessage(target.getPlayer(), command.getString("commands.socialspy.player.enabled"));
            }
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.enable");
            return true;
        }

        if (args.equalsIgnoreCase("off")) {
            if (target == null){

                if (!(playerSpy.isSocialSpyMode())){
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.unactivated"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                playerSpy.toggleSocialSpy(false);
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.disabled"));
                Bukkit.getLogger().info(String.valueOf(playerSpy.isSocialSpyMode()));
            } else {

                if (!target.isOnline()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.player-offline"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                String targetname = target.getName();
                UserCache targetSpy = manager.getCache().getPlayerUUID().get(target.getUniqueId());

                if (!(targetSpy.isSocialSpyMode())){
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.arg-2-unactivated").replace("%arg-2%", targetname));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }

                targetSpy.toggleSocialSpy(false);

                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.arg-2.disabled").replace("%arg-2%", targetname));
                playersender.sendMessage(target.getPlayer(), command.getString("commands.socialspy.player.disabled"));
            }
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.disable");
            return true;

        } else {
            playersender.sendMessage(player, messages.getString("error.unknown-arg"));
            pathManager.getUsage(player, "socialspy",  "on, off, list", "<player>");
            sound.setSound(player.getUniqueId(), "sounds.error");

        }

        return true;
    }

}
