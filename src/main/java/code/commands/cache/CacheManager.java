package code.commands.cache;

import code.Manager;
import code.debug.ErrorManager;

public class CacheManager {

    private final IgnoreCache ignoreCache;
    private final ReplyCache replyCache;
    private final SocialSpyCache socialSpyCache;

    private final Manager manager;


    public CacheManager(Manager manager){
        this.manager = manager;

        ErrorManager debug = manager.getLogs();
        ignoreCache = new IgnoreCache();
        debug.log("IgnoreCache loaded");
        replyCache = new ReplyCache();
        debug.log("ReplyCache loaded!");
        socialSpyCache = new SocialSpyCache();
        debug.log("SocialspyCache loaded!");
    }

    public IgnoreCache getIgnorelist(){
        return ignoreCache;
    }

    public ReplyCache getReply(){
        return replyCache;
    }

    public SocialSpyCache getSocialSpy(){
        return socialSpyCache;
    }
}
