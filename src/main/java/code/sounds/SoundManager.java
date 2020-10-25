package code.sounds;

import code.CacheManager;
import code.Manager;
import code.debug.ErrorManager;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class SoundManager{

    private final Manager manager;
    private final Configuration sound;

    private final CacheManager cache;
    private final ErrorManager debug;

    public SoundManager(Manager manager){
        this.manager = manager;
        this.cache = manager.getCache();
        this.sound = manager.getFiles().getSounds();
        this.debug = manager.getLogs();
    }

    public void setup() {

        String version = Bukkit.getServer().getClass().getName();

        String versionname = version.split("\\.")[3];


        switch (versionname) {
            case "v1_8_R3":
                debug.log("Using 1.8, enabling sounds.");
                break;
            case "v1_9_R2":
                debug.log("Using 1.9, enabling sounds.");
                break;
            default:

                debug.log("Using 1.9+, warning the sounds are from 1.8, 1.9, disabling it", 1);
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

        if (!(cache.getPlayerSounds().contains(target))){
            return;
        }

        String subpath = path.split("\\.")[1];

        if (subpath.equalsIgnoreCase("on-socialspy")) {
            if (!(sound.getBoolean("sounds.on-socialspy.enabled-all"))) {
                return;
            }
        }

        if (sound.getBoolean(path + ".enabled")) {
            player.getPlayer().playSound(location, getSound(path + ".sound"), sound.getInt("sounds.vol"), sound.getInt(path + ".pitch"));
        }

    }

    private Sound getSound(String path){
        return Sound.valueOf(sound.getString(path));

    }
}
