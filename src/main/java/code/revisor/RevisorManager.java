package code.revisor;

import code.Manager;
import code.revisor.message.FloodRevisor;
import code.revisor.message.WordRevisor;
import code.revisor.message.DotRevisor;
import code.revisor.message.LinkRevisor;
import code.utils.module.ModuleCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class RevisorManager{


    private Manager manager;

    private static Plugin plugin;
    private static ModuleCheck path;

    private final CooldownData cooldownData;
    private final FloodRevisor floodRevisor;
    private final WordRevisor wordRevisor;
    private final LinkRevisor linkRevisor;
    private final DotRevisor dotRevisor;

    public RevisorManager(Manager manager) {
        this.manager = manager;
        manager.getListManager().getModules().add("chat_revisor");
        cooldownData = new CooldownData(manager);
        floodRevisor = new FloodRevisor(manager);
        wordRevisor = new WordRevisor(manager);
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

        if (message == null){
            return null;
        }

        if (CooldownData.isSpamming(uuid)) {
            return null;
        }

        message = FloodRevisor.check(player, message);
        message = WordRevisor.check(player, message);
        message = LinkRevisor.check(player, message);

        return DotRevisor.check(message);
    }

    public LinkRevisor getLinkRevisor() {
        return linkRevisor;
    }

    public FloodRevisor getAntiFloodRevisor() {
        return floodRevisor;
    }

    public CooldownData getAntiRepeatRevisor() {
        return cooldownData;
    }

    public WordRevisor getBadWordsRevisor() {
        return wordRevisor;
    }

    public DotRevisor getDotRevisor() {
        return dotRevisor;
    }
}
