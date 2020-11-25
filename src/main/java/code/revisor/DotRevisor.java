package code.revisor;

import code.Manager;
import code.utils.Configuration;
import org.bukkit.Bukkit;

public class DotRevisor {


    private Manager manager;
    private static Configuration utils;

    public DotRevisor(Manager manager){
        this.manager = manager;
        utils = manager.getFiles().getBasicUtils();

    }

    public static String revisor(String string){

        if (!(utils.getBoolean("utils.chat.security.dot-module.enabled"))){
            return string;
        }

        int lettermin = utils.getInt("utils.chat.security.dot-module.min-word");

        if (string.length() > lettermin){
           string = string + ".";
           return string;
        }

        return string;
    }
}
