package code.bukkitutils.gui.manager;

import code.Manager;
import code.bukkitutils.gui.SampleManager;
import code.cache.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private Manager manager;
    private final Map<String, GuiData> inventorymanager;

    private final SampleManager sampleManager;

    public GuiManager(Manager manager) {
        this.manager = manager;
        inventorymanager = new HashMap<>();
        sampleManager = new SampleManager(manager);
    }

    public void createInventory(String name, String title, int size) {
        inventorymanager.put(name, new GuiData(title, size * 9));
    }

    public void openInventory(UUID uuid, String name, int page) {

        Player player = Bukkit.getPlayer(uuid);

        // User Cache
        UserCache userCache = manager.getCache().getPlayerUUID().get(uuid);

        switch (name){
            case "online":
                sampleManager.getOnlineSample().getPage(uuid, page);
                player.openInventory(inventorymanager.get(name).getInv());
        }

        userCache.setGUIGroup(name);
    }

    public GuiData getInventory(String name) {
        return inventorymanager.get(name);

    }

    public SampleManager getSampleManager(){
        return sampleManager;
    }

}
