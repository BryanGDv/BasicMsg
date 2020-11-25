package code.modules;

import code.Manager;
import code.modules.player.PlayerMessage;
import code.modules.player.PlayerStatic;
import code.utils.Configuration;
import code.utils.VariableManager;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.security.acl.Group;
import java.util.List;
import java.util.Set;

public class ListenerManaging {

    private final Manager manager;

    public ListenerManaging(Manager manager){
        this.manager = manager;
        manager.getListManager().getModules().add("join_quit");
        manager.getListManager().getModules().add("motd");
    }

    public void setJoin(PlayerJoinEvent event){
        Configuration utils = manager.getFiles().getBasicUtils();

        if (!(utils.getBoolean("utils.join.enabled"))){
            return;
        }

        Player player = event.getPlayer();

        event.setJoinMessage(PlayerStatic.setFormat(player, utils.getString("utils.join.format").replace("%player%", player.getName())));

        if (manager.getPathManager().isOptionEnabled("motd")){
            sendMotd(player);
        }
    }

    public void sendMotd(Player player){

        Configuration utils = manager.getFiles().getBasicUtils();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        VariableManager variable = manager.getVariables();

        List<String> motd = utils.getStringList("utils.join.motd.format");
        motd.replaceAll(text -> variable.replacePlayerVariables(player, text));

        for (int i = 0; i < utils.getInt("utils.join.motd.loop-blank"); i++){
            playersender.sendMessage(player, "");
        }

        for (String motdPath : motd){
            playersender.sendMessage(player, motdPath);
        }
    }

    public void setQuit(PlayerQuitEvent event){
        Configuration utils = manager.getFiles().getBasicUtils();

        if (!(utils.getBoolean("utils.quit.enabled"))){
            return;
        }
        Player player = event.getPlayer();

        event.setQuitMessage(PlayerStatic.setFormat(player, utils.getString("utils.quit.format")
                .replace("%player%", player.getName())));
    }

}
