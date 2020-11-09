package code.bukkitutils;

import code.Manager;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.*;

public class WorldManager{

    private static final Map<String, List<String>> worlds = new HashMap<>();

    private final Manager manager;
    private static Configuration utils;

    public WorldManager(Manager manager){
        this.manager = manager;
        utils = manager.getFiles().getBasicUtils();
    }

    public static List<String> getWorldChat(String world){
        if (worlds.containsKey(world)){
            return worlds.get(world);
        }

        List<String> worldsfile = utils.getStringList("utils.chat.per-world-chat.worlds." + world);
        worldsfile.add(world);

        worlds.put(world, worldsfile);
        return worldsfile;
    }

    public static List<String> getAllWorldChat(){

        List<String> worldNames = new ArrayList<>();

        List<String> exceptfolders = Arrays.asList("plugins","logs", "cache");

        for (File file : Bukkit.getServer().getWorldContainer().listFiles()){
            if (file.isDirectory() && (!(exceptfolders.contains(file.getName())))){
                worldNames.add(file.getName());
            }
        }

        return worldNames;
    }
}
