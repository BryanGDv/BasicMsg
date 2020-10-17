package code.commands.cache;

import code.interfaces.Cache;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class IgnoreCache implements Cache<UUID, List<String>>{

    private final HashMap<UUID, List<String>> ignorelist = new HashMap<>();

    public Map<UUID, List<String>> get() {
        return ignorelist;
    }


}
