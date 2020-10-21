package code.modules;

import code.Manager;
import me.clip.placeholderapi.util.Msg;

public class MethodManager{

    private SocialSpyMethod socialSpyMethod;
    private IgnoreMethod ignoreMethod;
    private PlayerMessage playerMessage;
    private MsgToggleMethod msgToggleMethod;

    private final Manager manager;

    public MethodManager(Manager manager){
        this.manager = manager;
    }

    public void setup(){
        ignoreMethod = new IgnoreMethod(manager);
        socialSpyMethod = new SocialSpyMethod(manager);
        msgToggleMethod = new MsgToggleMethod(manager);
        playerMessage = new PlayerMessage(manager);

        manager.getLogs().log("Method registered");

    }

    public MsgToggleMethod getMsgToggleMethod(){
        return msgToggleMethod;
    }
    public SocialSpyMethod getSocialSpyMethod(){
        return socialSpyMethod;
    }

    public IgnoreMethod getIgnoreMethod(){
        return ignoreMethod;
    }

    public PlayerMessage getSender(){
        return playerMessage;
    }
}
