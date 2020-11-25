package code.events;

import code.CacheManager;
import code.Manager;
import code.cache.UserCache;
import code.modules.ListenerManaging;
import code.modules.player.PlayerMessage;
import code.utils.Configuration;
import code.utils.PathManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener{

    private final Manager manager;

    public QuitListener(Manager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CacheManager cache = manager.getCache();
        PlayerMessage player = manager.getPlayerMethods().getSender();
        Configuration command = manager.getFiles().getCommand();

        PathManager pathManager = manager.getPathManager();
        ListenerManaging listenerManaging = manager.getPlayerMethods().getListenerManaging();


        Player you = event.getPlayer();
        UserCache playerStatus = manager.getCache().getPlayerUUID().get(you.getUniqueId());

        if (pathManager.isOptionEnabled("join_quit")){
            listenerManaging.setQuit(event);
        }

        if (pathManager.isCommandEnabled("reply")) {
            if (playerStatus.hasRepliedPlayer()){

                UserCache target = manager.getCache().getPlayerUUID().get(playerStatus.getRepliedPlayer());

                if (target.hasRepliedPlayer()) {

                    if (target.hasRepliedPlayer(you.getUniqueId())) {
                        target.setRepliedPlayer(null);
                    }

                    player.sendMessage(target.getPlayer(), command.getString("commands.msg-toggle.left")
                            .replace("%player%", target.getPlayer().getName())
                            .replace("%arg-1%", event.getPlayer().getName())); }



            }

        }

        playerStatus.resetStats();

    }

}
