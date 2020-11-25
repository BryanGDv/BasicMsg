package code.bukkitutils;

import code.Manager;
import code.bukkitutils.gui.manager.GuiManager;

public class ManagingCenter {


    private final Manager manager;

    private SoundManager soundManager;
    private WorldManager worldManager;
    private RunnableManager runnableManager;
    private GuiManager guiManager;


    public ManagingCenter(Manager manager){
        this.manager = manager;
        setup();
    }

    public void setup(){
        soundManager = new SoundManager(manager);
        worldManager = new WorldManager(manager);
        guiManager = new GuiManager(manager);
        runnableManager = new RunnableManager(manager);
    }

    public GuiManager getGuiManager(){
        return guiManager;
    }
    public WorldManager getWorldManager() {
        return worldManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public RunnableManager getRunnableManager() {
        return runnableManager;
    }

}
