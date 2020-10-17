package code.modules;

import code.Manager;
import code.commands.cache.CacheManager;
import code.commands.cache.IgnoreCache;
import code.utils.Configuration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IgnoreMethod {

    private final Configuration players;
    private final Configuration messages;

    private final Manager manager;

    private final CacheManager cache;



    public IgnoreMethod(Manager manager) {
        this.manager = manager;

        // All methods:
        this.cache = manager.getCache();
        this.players = manager.getFiles().getPlayers();
        this.messages = manager.getFiles().getMessages();

    }


    public void add(CommandSender sender, Player player) {

        Player you = (Player) sender;
        UUID uuid = you.getUniqueId();

        IgnoreCache ignorelist = cache.getIgnorelist();

        List<String> ignoredPlayers;

        if (ignorelist.get().get(uuid) == null) {
            ignoredPlayers = new ArrayList<>();
        } else {
            ignoredPlayers = ignorelist.get().get(uuid);
        }

        ignoredPlayers.add(player.getName());
        ignorelist.get().put(uuid, ignoredPlayers);
        players.set("players." + uuid + ".players-ignored", ignoredPlayers);

        players.save();


    }

    public void remove(CommandSender sender, Player player){

        Player you = (Player) sender;
        UUID uuid = you.getUniqueId();

        IgnoreCache ignorelist = cache.getIgnorelist();
        List<String> ignoredPlayers = ignorelist.get().get(uuid);

        ignoredPlayers.remove(player.getName());
        players.set("players." + uuid + ".players-ignored", ignoredPlayers);
        players.save();

        if (players.getStringList("players." + uuid + ".players-ignored").isEmpty()){
            players.set("players." + uuid, null);
            players.save();
        }

    }
}
