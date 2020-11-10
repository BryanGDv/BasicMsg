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
    public boolean isOptionEnabled(String optionName) {
        List<String> optionFile = config.getStringList("config.modules.enabled-options");
        for (String optionEnabledOptions : optionFile) {
            if (optionEnabledOptions.equalsIgnoreCase(optionName)) {
                return true;
            }
        }
        return false;
    }
}