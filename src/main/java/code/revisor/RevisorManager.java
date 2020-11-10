package code.revisor;

import code.Manager;
import code.utils.PathManager;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class RevisorManager{


    private Manager manager;

    private static Plugin plugin;
    private static PathManager path;

    private AntiRepeatRevisor antiRepeatRevisor;
    private AntiFloodRevisor antiFloodRevisor;

    public RevisorManager(Manager manager) {
        this.manager = manager;
        antiRepeatRevisor = new AntiRepeatRevisor(manager);
        antiFloodRevisor = new AntiFloodRevisor(manager);
        plugin = manager.getPlugin();
        path = manager.getPathManager();
    }

    public static String revisor(UUID uuid, String string) {
        if (!(path.isOptionEnabled("chat_revisor"))){
            return string;
        }

        String stringflooded = AntiFloodRevisor.revisor(string);

        if (AntiRepeatRevisor.isSpamming(uuid)) {
            return null;
        }

        return stringflooded;
    }

}
