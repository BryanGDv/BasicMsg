package code.commands.cache;

import code.interfaces.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplyCache implements Cache<UUID, UUID> {

    private final HashMap<UUID, UUID> reply = new HashMap<>();

    @Override
    public Map<UUID, UUID> get() {
        return reply;
    }

}
