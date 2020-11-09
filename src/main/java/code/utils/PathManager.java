package code.utils;


import code.Manager;

import java.util.List;

public class PathManager {

    private Manager manager;
    private Configuration config;

    public PathManager(Manager manager) {
        this.manager = manager;
        this.config = manager.getFiles().getConfig();
    }

    public boolean isCommandEnabled(String commandName) {
        List<String> commandFile = config.getStringList("config.modules.enabled-commands");
        for (String commandEnabledCmds : commandFile) {
            if (commandEnabledCmds.equalsIgnoreCase(commandName)) {
                return true;
            }
        }
        return false;
    }
}