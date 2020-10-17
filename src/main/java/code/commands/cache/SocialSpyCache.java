package code.commands.cache;


import java.util.*;

public class SocialSpyCache {
    private final HashSet<UUID> socialspy;

    public SocialSpyCache(){
        socialspy = new HashSet<>();
    }


    public HashSet<UUID> get() {
        return socialspy;
    }

}
