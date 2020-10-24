package code.sounds;

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
    private final ErrorManager debug;

    public SoundManager(Manager manager){
        this.manager = manager;
        this.sound = manager.getFiles().getSounds();
        this.debug = manager.getLogs();
    }

    public void setup() {

        String version = Bukkit.getServer().getClass().getName();

        String test3 = version.split("\\.")[3];

        Bukkit.getLogger().info("Normal: " + version);
        Bukkit.getLogger().info("Version: " + test3);

        switch (test3) {
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
