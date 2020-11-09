package code.commands;

import code.CacheManager;
import code.modules.MsgToggleMethod;
import code.modules.ReplyMethod;
import code.modules.player.PlayerStatic;
import code.registry.ConfigManager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.utils.Configuration;
import code.Manager;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MsgCommand implements CommandClass{

    private final Manager manager;
    private final CacheManager cache;

    public MsgCommand(Manager manager, CacheManager cache){
        this.manager = manager;
        this.cache = cache;
    }

    @Command(names = {"msg", "pm", "tell", "t", "w"})

    public boolean onCommand(CommandSender sender, @OptArg OfflinePlayer target, @OptArg String msg) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        MsgToggleMethod msgToggleMethod = manager.getPlayerMethods().getMsgToggleMethod();
        SoundManager sound = manager.getSounds();

        Configuration players = files.getPlayers();
        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration messages = files.getMessages();

        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;
        UUID playeruuid = player.getUniqueId();

        if (!(manager.getPathManager().isCommandEnabled("msg"))){
            playersender.sendMessage(sender, messages.getString("error.command-disabled")
                    .replace("%player%", player.getName())
                    .replace("%command%", "msg"));
            return true;
        }

        if (target == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/msg [player] [message]");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        UUID targetuuid = target.getUniqueId();

        if (!(target.isOnline()) && (!(target.getName().equalsIgnoreCase("-toggle")))){
            playersender.sendMessage(sender, messages.getString("error.player-offline"));
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }

        if (target.getName().equalsIgnoreCase(sender.getName())) {
            playersender.sendMessage(sender, messages.getString("error.same-player")
                    .replace("%player%", sender.getName()));
            sound.setSound(playeruuid, "sounds.error");
            return true;

        }

        Set<UUID> msgtoggle = cache.getMsgToggle();
        if (target.getName().equalsIgnoreCase("-toggle")){

            if (msg == null){

                if (!(msgtoggle.contains(playeruuid))){
                    msgToggleMethod.set(playeruuid);
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.player.activated"));
                }else{
                    msgToggleMethod.unset(playeruuid);
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.player.unactivated"));
                }

            } else {

                OfflinePlayer you = Bukkit.getPlayer(msg);

                if (!(sender.hasPermission(config.getString("config.perms.toggle-admin")))){
                    playersender.sendMessage(sender, messages.getString("error.no-perms"));
                    sound.setSound(playeruuid, "sounds.error");
                    return true;
                }

                if (you == null){
                    playersender.sendMessage(sender, messages.getString("error.player-offline"));
                    sound.setSound(playeruuid, "sounds.error");
                    return true;
                }

                CommandSender yousender =  you.getPlayer();

                if (!(msgtoggle.contains(you.getUniqueId()))){
                    msgToggleMethod.set(you.getUniqueId());
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.arg-1.activated")
                            .replace("%arg-1%", yousender.getName()));
                    playersender.sendMessage(yousender, command.getString("commands.msg-toggle.player.activated"));
                }else{
                    msgToggleMethod.unset(you.getUniqueId());
                    playersender.sendMessage(sender, command.getString("commands.msg-toggle.arg-1.unactivated")
                            .replace("%arg-1%", yousender.getName()));
                    playersender.sendMessage(yousender, command.getString("commands.msg-toggle.player.unactivated"));
                }
                sound.setSound(player.getUniqueId(), "sounds.on-togglepm");
            }
            return true;
        }

        if (msgtoggle.contains(target.getUniqueId())){
            playersender.sendMessage(sender, command.getString("commands.msg-toggle.msg")
                    .replace("%player%",target.getName()));
            return true;
        }


        if (msg == null) {
            playersender.sendMessage(sender, messages.getString("error.no-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/msg [player] [message]");
            sound.setSound(playeruuid, "sounds.error");
            return true;
        }


        String message = String.join(" ", msg);

        if (sender.hasPermission(config.getString("config.perms.msg-color"))) {
            message = PlayerStatic.setColor(msg);
        }

        String playerFormat = command.getString("commands.msg-reply.player");
        String targetFormat = command.getString("commands.msg-reply.arg-1");


        playersender.sendMessage(player, playerFormat

                .replace("%player%", sender.getName())
                .replace("%arg-1%", target.getName())
                , message, true);
        sound.setSound(player.getUniqueId(), "sounds.on-message");

        List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

        if (!(ignoredlist.contains(target.getName()))){
            playersender.sendMessage(target.getPlayer(), targetFormat
                            .replace("%player%", sender.getName())
                            .replace("%arg-1%", target.getName())
                    , message, true);
            sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-receive-msg");

        }
        for (Player watcher : Bukkit.getServer().getOnlinePlayers()) {
            if (cache.getSocialSpy().contains(watcher.getUniqueId())) {
                playersender.sendMessage(watcher, command.getString ("commands.socialspy.spy")
                        .replace("%player%", sender.getName())
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
