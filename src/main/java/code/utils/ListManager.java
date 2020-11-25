package code.utils;

import code.Manager;

import java.util.ArrayList;
import java.util.List;

public class ListManager {

    private Manager manager;

    private List<String> commandsList;
    private List<String> modulesList;

    public ListManager(Manager manager){
        this.manager = manager;
        setup();
    }

    public void setup(){
        commandsList = new ArrayList<>();
        modulesList = new ArrayList<>();
    }
    public List<String> getCommands(){
        return commandsList;
    }

    public List<String> getModules(){
        return modulesList;
    }
}
