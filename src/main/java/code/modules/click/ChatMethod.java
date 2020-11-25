package code.modules.click;

import code.Manager;
import code.bukkitutils.RunnableManager;
import code.bukkitutils.WorldManager;
import code.cache.UserCache;
import code.modules.MethodService;
import code.modules.player.PlayerMessage;
import code.modules.player.PlayerStatic;
import code.utils.Configuration;
import code.utils.HoverManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatMethod {

    private Manager manager;
    private Boolean worldtype;

    public ChatMethod(Manager manager) {
        this.manager = manager;
    }

    public void setAgain(UUID uuid) {
        setWorld(uuid);
    }

    public void activateChat(UUID uuid, Boolean world) {

        worldtype = world;
        setWorld(uuid);
    }

    private void setWorld(UUID uuid){

        UserCache userCache = manager.getCache().getPlayerUUID().get(uuid);

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        RunnableManager runnableManager = manager.getManagingCenter().getRunnableManager();

        Player player = Bukkit.getPlayer(uuid);

        Configuration command = manager.getFiles().getCommand();
        Configuration messages = manager.getFiles().getMessages();

        List<String> chatClick = userCache.getClickChat();

        if (chatClick.size() < 1) {
            userCache.toggleClickMode(true);
            playersender.sendMessage(player, command.getString("commands.broadcast.mode.load"));
            playersender.sendMessage(player, command.getString("commands.broadcast.mode.select.message"));
            playersender.sendMessage(player, "&ePut &6\"-cancel\" &eto cancel the clickchat mode.");
            return;
        }

        if (chatClick.size() < 2){
            runnableManager.waitSecond(player, 1,
                    command.getString("commands.broadcast.mode.select.command"),
                    "&aYou dont need to put '/' in this case." );
            return;
        }

        if (chatClick.size() < 3){
            runnableManager.waitSecond(player, 1,
                    command.getString("commands.broadcast.mode.select.cooldown"),
                    "&aIf you want now, use &8[&f-now&8]&a." );
            return;
        }


        if (chatClick.size() == 3){

            int cooldown;

            try{
                cooldown = Integer.parseInt(chatClick.get(2));
            } catch (NumberFormatException nf){
                playersender.sendMessage(player, messages.getString("error.chat.unknown-number")
                        .replace("%number%", chatClick.get(2)));
                userCache.toggleClickMode(true);
                chatClick.remove(2);
                return;
            }

            runnableManager.waitSecond(player, 1, command.getString("commands.broadcast.mode.hover"));

            waitHover(player, cooldown);

        }
    }



    public void waitHover(Player player, int second){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), new Runnable() {
            @Override
            public void run() {

                Configuration command = manager.getFiles().getCommand();

                UserCache userCache = manager.getCache().getPlayerUUID().get(player.getUniqueId());
                List<String> chatClick = userCache.getClickChat();

                HoverManager hover;
                if (worldtype){
                    hover = new HoverManager(PlayerStatic.setColor(command.getString("commands.broadcast.click_cmd.world")
                            .replace("%message%", chatClick.get(0))
                            .replace("%player%", player.getName())
                            .replace("%world%", player.getWorld().getName())));
                }else{
                    hover = new HoverManager(PlayerStatic.setColor(command.getString("commands.broadcast.click_cmd.global")
                            .replace("%message%", chatClick.get(0))
                            .replace("%player%", player.getName())));
                }

                hover.setHover(command.getString("commands.broadcast.click_cmd.format"), "/" + chatClick.get(1), true);

                Configuration utils = manager.getFiles().getBasicUtils();

                if (worldtype) {
                    List<Player> worldList;

                    if (utils.getBoolean("utils.chat.per-world-chat.all-worlds")){
                        worldList = getWorldChat(player);
                    }else{
                        worldList = getWorldList(player);
                    }

                    for (Player playeronline : worldList) {
                        playeronline.spigot().sendMessage(hover.getTextComponent());
                    }
                }else{
                    for (Player playeronline : Bukkit.getServer().getOnlinePlayers()){
                        playeronline.spigot().sendMessage(hover.getTextComponent());
                    }
                }

                userCache.toggleClickMode(false);
                chatClick.clear();
            }
        }, 20L * second);
    }

    public List<Player> getWorldList(Player player){
        List<Player> listplayer = new ArrayList<>();
        for (String worldname : WorldManager.getWorldChat(player.getWorld().getName())) {
            World world = Bukkit.getWorld(worldname);
            listplayer.addAll(world.getPlayers());
        }
        return listplayer;
    }

    public List<Player> getWorldChat(Player player){
        for (String worldname : WorldManager.getAllWorldChat()) {
            if (player.getWorld().getName().equalsIgnoreCase(worldname)) {
                World world = Bukkit.getWorld(worldname);
                return world.getPlayers();

            }
        }
        return null;
    }

    public void unset(UUID uuid) {

        Player player = Bukkit.getPlayer(uuid);

        UserCache userCache = manager.getCache().getPlayerUUID().get(player.getUniqueId());
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration command = manager.getFiles().getCommand();

        playersender.sendMessage(player, command.getString("commands.broadcast.mode.disabled"));
        userCache.toggleClickMode(false);
        userCache.getClickChat().clear();
    }
}
