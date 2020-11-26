package code.methods;

import code.Manager;
import code.utils.Configuration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class GroupMethod {

    private final Manager manager;

    public GroupMethod(Manager manager){
        this.manager = manager;
    }
    public Set<String> getGroup(){

        Configuration utils = manager.getFiles().getBasicUtils();

        return utils.getConfigurationSection("utils.chat.groups").getKeys(false);
    }
    public String getPlayerGroup(Player player){
        Configuration utils = manager.getFiles().getBasicUtils();

        if (player.isOp()){
            return "default";
        }

        for (String group : getGroup()){

            if (group.equalsIgnoreCase("default")){
                continue;
            }

            if (player.hasPermission(utils.getString("utils.chat.groups." + group + ".permission"))){
                return group;
            }
        }

        return "default";
    }

    public String getPlayerFormat(Player player){
        Configuration utils = manager.getFiles().getBasicUtils();

        return utils.getString("utils.chat.groups." + getPlayerGroup(player) + ".format");

    }

    public List<String> getPlayerHover(Player player){
        Configuration utils = manager.getFiles().getBasicUtils();

        return utils.getStringList("utils.chat.groups." + getPlayerGroup(player) + ".hover");

    }

    public String getPlayerCmd(Player player){
        Configuration utils = manager.getFiles().getBasicUtils();

        return utils.getString("utils.chat.groups." + getPlayerGroup(player) + ".command");

    }

    public boolean isChannelEnabled(Player player){
        Configuration utils = manager.getFiles().getBasicUtils();
        return utils.getBoolean("utils.chat.groups." + getPlayerGroup(player) + ".channel");
    }

    public boolean channelNotExists(String group){
        Configuration utils = manager.getFiles().getBasicUtils();

        return utils.getString("utils.chat.groups." + group) == null;
    }


    public boolean isChannelEnabled(String group){
        Configuration utils = manager.getFiles().getBasicUtils();

        if (group.equalsIgnoreCase("default")){
            return true;
        }
        return utils.getBoolean("utils.chat.groups." + group + ".channel");
    }

    public boolean hasGroupPermission(Player player, String group){
        Configuration utils = manager.getFiles().getBasicUtils();

        if (group.equalsIgnoreCase("default")){
            return true;
        }

        return player.hasPermission(utils.getString("utils.chat.groups." + group + ".permission"));
    }

    public boolean isChannel(Player player, String group){
        Configuration utils = manager.getFiles().getBasicUtils();
        return this.getPlayerGroup(player).equalsIgnoreCase(group);

    }
}
