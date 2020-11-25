package code.bukkitutils;

import code.CacheManager;
import code.Manager;
import code.cache.UserCache;
import code.debug.DebugLogger;
import code.modules.player.PlayerMessage;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.UUID;

public class SoundManager{


    private final Manager manager;
    private final Configuration sound;

    private final CacheManager cache;
    private final DebugLogger debug;

    public SoundManager(Manager manager){
        this.manager = manager;
        this.cache = manager.getCache();
        this.sound = manager.getFiles().getSounds();
        this.debug = manager.getLogs();
    }

    public void setup() {

        String version = Bukkit.getServer().getClass().getName();

        String versionname = version.split("\\.")[3];

        if (versionname.contains("8")) {
            debug.log("Using 1.8, enabling sounds. Version:" + versionname);

        }if (versionname.contains("9")) {
            debug.log("Using 1.9, enabling sounds. Version:" + versionname);

        }else{
            debug.log("Using 1.9+, warning the sounds are from 1.8, 1.9, disabling it", 0);
            debug.log("Modify the sound, to avoid errors");
            sound.set("config.enabled-all", false);
        }

    }

    public void setSound(UUID target, String path){
        OfflinePlayer player = Bukkit.getPlayer(target);

        Location location = player.getPlayer().getLocation();

        if (!(sound.getBoolean("sounds.enabled-all"))){
            return;
        }

        UserCache playerMsgToggle = manager.getCache().getPlayerUUID().get(target);
        if (!(playerMsgToggle.isPlayersoundMode())){
            return;
        }

        String subpath = path.split("\\.")[1];

        if (subpath.equalsIgnoreCase("on-socialspy")) {
            if (!(sound.getBoolean("sounds.on-socialspy.enabled-all"))) {
                return;
            }
        }

        if (subpath.equalsIgnoreCase("on-helpop")) {
            if (!(sound.getBoolean("sounds.on-helpop.enabled-all"))) {
                return;
            }
        }

        Sound soundType = getSound(path + ".sound");

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        Configuration messages = manager.getFiles().getMessages();

        if (soundType == null) {
            playersender.sendMessage(player.getPlayer(), messages.getString("error.sound.no-exists"));
            return;
        }

        if (sound.getBoolean(path + ".enabled")) {
            player.getPlayer().playSound(location, soundType, sound.getInt("sounds.vol"), sound.getInt(path + ".pitch"));
        }

    }
    private Sound getSound(String path){
        try {
            return Sound.valueOf(sound.getString(path));

        }catch( IllegalArgumentException io){
            manager.getLogs().log("Sound_Error" + io.getCause());

            for (StackTraceElement stack : io.getStackTrace()){
                manager.getLogs().log(stack.toString());
            }

            return null;
        }
    }
}
