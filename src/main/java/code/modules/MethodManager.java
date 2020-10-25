package code.modules;

import code.Manager;
import code.modules.player.PlayerMessage;

public class MethodManager{

    private SocialSpyMethod socialSpyMethod;
    private IgnoreMethod ignoreMethod;
    private PlayerMessage playerMessage;
    private MsgToggleMethod msgToggleMethod;
    private PlayerSoundMethod playerSoundMethod;

    private final Manager manager;

    public MethodManager(Manager manager){
        this.manager = manager;
    }

    public void setup(){
        ignoreMethod = new IgnoreMethod(manager);
        socialSpyMethod = new SocialSpyMethod(manager);
        msgToggleMethod = new MsgToggleMethod(manager);
        playerMessage = new PlayerMessage(manager);
        playerSoundMethod = new PlayerSoundMethod(manager);

        manager.getLogs().log("Method registered");

    }

    public MsgToggleMethod getMsgToggleMethod(){
        return msgToggleMethod;
    }
    public PlayerSoundMethod getPlayerSoundMethod(){
        return playerSoundMethod;
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
