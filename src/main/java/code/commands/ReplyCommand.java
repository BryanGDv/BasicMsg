package code.commands;

import code.CacheManager;
import code.cache.UserCache;
import code.modules.player.PlayerStatic;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import code.utils.PathManager;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplyCommand implements CommandClass {

    private final Manager manager;
    private final CacheManager cache;

    public ReplyCommand(Manager manager, CacheManager cache){
        this.manager = manager;
        this.cache = cache;
    }
    @Command(names = {"reply", "r"})
    public boolean onCommand(@Sender Player player, @OptArg @Text String message) {

        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getManagingCenter().getSoundManager();
        PathManager pathManager = manager.getPathManager();

        Configuration players = files.getPlayers();
        Configuration config = files.getConfig();
        Configuration command = files.getCommand();
        Configuration lang = files.getMessages();

        UUID playeruuid = player.getUniqueId();

        if (!(pathManager.isCommandEnabled("reply"))) {
            pathManager.sendDisabledCmdMessage(player, "reply");
            return true;
        }


        if (message.trim().isEmpty()) {
            playersender.sendMessage(player, lang.getString("error.no-arg"));
            pathManager.getUsage(player, "reply",  "<message>");
            sound.setSound(player.getUniqueId(), "sounds.error");

            return true;
        }


        UserCache playerCache = manager.getCache().getPlayerUUID().get(player.getUniqueId());

        if (playerCache.hasRepliedPlayer()){

            OfflinePlayer target = Bukkit.getPlayer(playerCache.getRepliedPlayer());

            if (message.equalsIgnoreCase("-player")){
                playersender.sendMessage(player, command.getString("commands.msg-reply.talked")
                .replace("%player%", target.getName()));
                sound.setSound(player.getUniqueId(), "sounds.on-reply-player");
                return true;
            }

            CommandSender targetsender = target.getPlayer();

            if (player.hasPermission(config.getString("config.perms.msg-color"))){
                message = PlayerStatic.setColor(message);
            }

            playersender.sendMessage(player, PlayerStatic.setColor(command.getString("commands.msg-reply.player")
                    .replace("%player%", player.getName())
                    .replace("%arg-1%", target.getName()))
                    , message, true);

            sound.setSound(player.getUniqueId(), "sounds.on-reply");

            List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

            if (!(ignoredlist.contains(target.getName()))) {
                playersender.sendMessage(targetsender, PlayerStatic.setColor(command.getString("commands.msg-reply.player")
                                .replace("%player%", player.getName())
                                .replace("%arg-1%", target.getName()))
                        , message, true);

                UserCache targetCache = manager.getCache().getPlayerUUID().get(target.getUniqueId());

                targetCache.setRepliedPlayer(playeruuid);

                sound.setSound(player.getUniqueId(), "sounds.on-reply");

            }

        } else {
            playersender.sendMessage(player, lang.getString("error.no-reply"));
            sound.setSound(player.getUniqueId(), "sounds.error");

        }
        return true;
    }
}


