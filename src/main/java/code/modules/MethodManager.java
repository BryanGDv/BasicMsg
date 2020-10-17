package code.modules;


import code.Manager;

public class MethodManager{

    private SocialSpyMethod socialSpyMethod;
    private IgnoreMethod ignoreMethod;
    private PlayerMessage playerMessage;

    private final Manager manager;

    public MethodManager(Manager manager){
        this.manager = manager;
    }

    public void setup(){
        ignoreMethod = new IgnoreMethod(manager);
        socialSpyMethod = new SocialSpyMethod(manager);
        playerMessage = new PlayerMessage(manager);

        manager.getLogs().log("Method registered");

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
