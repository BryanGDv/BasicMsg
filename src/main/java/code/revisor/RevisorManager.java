package code.revisor;

import code.Manager;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class RevisorManager{


    private Manager manager;
    private static Plugin plugin;

    public RevisorManager(Manager manager) {
        this.manager = manager;
        plugin = manager.getPlugin();
    }

    public static String revisor(UUID uuid, String string) {
        String stringflooded = AntiFloodRevisor.revisor(string);
        if (AntiRepeatRevisor.isSpamming(uuid)) {
            return null;
        }

        return stringflooded;
    }
}
