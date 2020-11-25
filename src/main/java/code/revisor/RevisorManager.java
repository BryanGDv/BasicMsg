package code.revisor;

import code.Manager;
import code.utils.PathManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import sun.awt.image.ImageWatched;

import java.util.UUID;

public class RevisorManager{


    private Manager manager;

    private static Plugin plugin;
    private static PathManager path;

    private final AntiRepeatRevisor antiRepeatRevisor;
    private final AntiFloodRevisor antiFloodRevisor;
    private final BadWordsRevisor badWordsRevisor;
    private final LinkRevisor linkRevisor;
    private final DotRevisor dotRevisor;

    public RevisorManager(Manager manager) {
        this.manager = manager;
        manager.getListManager().getModules().add("chat_revisor");
        antiRepeatRevisor = new AntiRepeatRevisor(manager);
        antiFloodRevisor = new AntiFloodRevisor(manager);
        badWordsRevisor = new BadWordsRevisor(manager);
        linkRevisor = new LinkRevisor(manager);
        dotRevisor = new DotRevisor(manager);
        plugin = manager.getPlugin();
        path = manager.getPathManager();
    }

    public static String revisor(UUID uuid, String message) {
        if (!(path.isOptionEnabled("chat_revisor"))){
            return message;
        }

        Player player = Bukkit.getPlayer(uuid);

        if (AntiRepeatRevisor.isSpamming(uuid)) {
            return null;
        }

        message = AntiFloodRevisor.revisor(player, message);

        if (message == null){
            return null;
        }

        message = BadWordsRevisor.revisor(player, message);

        message = LinkRevisor.revisor(player, message);

        return DotRevisor.revisor(message);
    }

    public LinkRevisor getLinkRevisor() {
        return linkRevisor;
    }

    public AntiFloodRevisor getAntiFloodRevisor() {
        return antiFloodRevisor;
    }

    public AntiRepeatRevisor getAntiRepeatRevisor() {
        return antiRepeatRevisor;
    }

    public BadWordsRevisor getBadWordsRevisor() {
        return badWordsRevisor;
    }

    public DotRevisor getDotRevisor() {
        return dotRevisor;
    }
}
