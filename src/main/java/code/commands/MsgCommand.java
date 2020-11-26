package code.commands;

import code.CacheManager;
import code.bukkitutils.gui.manager.GuiManager;
import code.cache.UserData;
import code.methods.commands.MsgMethod;
import code.methods.commands.ReplyMethod;
import code.methods.player.PlayerStatic;
import code.registry.ConfigManager;
import code.methods.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import code.utils.module.ModuleCheck;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.utils.Configuration;
import code.Manager;

import java.util.UUID;

public class MsgCommand implements CommandClass{

    private final Manager manager;
    private final CacheManager cache;

    public MsgCommand(Manager manager, CacheManager cache){
        this.manager = manager;
        this.cache = cache;
    }

    @Command(names = {"msg", "pm", "tell", "t", "w", "whisper"})

    public boolean onCommand(@Sender Player player, @OptArg OfflinePlayer target, @OptArg @Text String msg) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getManagingCenter().getSoundManager();
        ModuleCheck moduleCheck = manager.getPathManager();
        
        Configuration players = files.getPlayers();
        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        UUID playeruuid = player.getUniqueId();

        if (!(moduleCheck.isCommandEnabled("msg"))) {
            moduleCheck.sendDisableMessage(player, "msg");
            return true;
        }

        if (target == null) {
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "msg", "<player>", "<message>");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        UUID targetuuid = target.getUniqueId();

        if (!(target.isOnline()) && !(target.getName().contains("-"))){
            playersender.sendMessage(player, messages.getString("error.player-offline"));
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (target.getName().equalsIgnoreCase(player.getName())) {
            playersender.sendMessage(player, messages.getString("error.same-player")
                    .replace("%player%", player.getName()));
            sound.setSound(playeruuid, "sounds.error");
            return true;

        }

        if (target.getName().equalsIgnoreCase("-online")){
            if (Bukkit.getServer().getOnlinePlayers().size() < 2) {
                playersender.sendMessage(player, messages.getString("error.nobody-offline"));
                return true;
            }

            GuiManager guiManager = manager.getManagingCenter().getGuiManager();
            guiManager.openInventory(playeruuid, "online", 0);
            return true;
        }

        UserData playerMsgToggle = manager.getCache().getPlayerUUID().get(playeruuid);

        if (target.getName().equalsIgnoreCase("-toggle")){

            if (msg.trim().isEmpty()){
                if (!(playerMsgToggle.isSocialSpyMode())){
                    playerMsgToggle.toggleSocialSpy(true);
                    playersender.sendMessage(player, command.getString("commands.msg-toggle.player.activated"));
                }else{
                    playerMsgToggle.toggleSocialSpy(false);
                    playersender.sendMessage(player, command.getString("commands.msg-toggle.player.unactivated"));
                }
                return true;

            } else {

                if (!(player.hasPermission(config.getString("config.perms.toggle-admin")))){
                    playersender.sendMessage(player, messages.getString("error.no-perms"));
                    sound.setSound(playeruuid, "sounds.error");
                    return true;
                }
                OfflinePlayer you = Bukkit.getPlayer(msg);

                if (you == null){
                    playersender.sendMessage(player, messages.getString("error.player-offline"));
                    sound.setSound(playeruuid, "sounds.error");
                    return true;
                }

                UserData targetMsgToggle = manager.getCache().getPlayerUUID().get(you.getUniqueId());
                CommandSender yousender =  you.getPlayer();

                if (!(targetMsgToggle.isMsgtoggleMode())){
                    targetMsgToggle.toggleMsg(true);
                    playersender.sendMessage(player, command.getString("commands.msg-toggle.arg-1.activated")
                            .replace("%arg-1%", yousender.getName()));
                    playersender.sendMessage(yousender, command.getString("commands.msg-toggle.player.activated"));
                }else{
                    targetMsgToggle.toggleMsg(false);
                    playersender.sendMessage(player, command.getString("commands.msg-toggle.arg-1.unactivated")
                            .replace("%arg-1%", yousender.getName()));
                    playersender.sendMessage(yousender, command.getString("commands.msg-toggle.player.unactivated"));
                }
                sound.setSound(player.getUniqueId(), "sounds.on-togglepm");
            }
            return true;
        }

        UserData targetToggled = manager.getCache().getPlayerUUID().get(targetuuid);

        if (targetToggled == null){
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "msg", "<player>", "<message>");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }
        if (targetToggled.isMsgtoggleMode()){
            playersender.sendMessage(player, command.getString("commands.msg-toggle.msg")
                    .replace("%player%",target.getName()));
            return true;
        }


        if (msg.trim().isEmpty()) {
            playersender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "msg", "<player>", "<message>");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        String message = String.join(" ", msg);

        if (player.hasPermission(config.getString("config.perms.msg-color"))) {
            message = PlayerStatic.setColor(msg);
        }

        MsgMethod msgMethod = manager.getPlayerMethods().getMsgMethod();
        Player targetplayer = target.getPlayer();

        msgMethod.sendPrivateMessage(player, targetplayer, message);

        for (Player watcher : Bukkit.getServer().getOnlinePlayers()) {
            UserData watcherSpy = manager.getCache().getPlayerUUID().get(watcher.getUniqueId());

            if (watcherSpy.isSocialSpyMode()){
                playersender.sendMessage(watcher, command.getString ("commands.socialspy.spy")
                        .replace("%player%", player.getName())
                        .replace("%arg-1%", target.getName())
                        .replace("%message%", message));
                sound.setSound(watcher.getPlayer().getUniqueId(), "sounds.on-socialspy.receive-msg");
            }
        }

        ReplyMethod reply = manager.getPlayerMethods().getReplyMethod();
        reply.setReply(playeruuid, targetuuid);

        return true;
        }
}
