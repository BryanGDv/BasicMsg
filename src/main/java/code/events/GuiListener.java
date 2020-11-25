package code.events;

import code.Manager;
import code.bukkitutils.gui.OnlineSample;
import code.bukkitutils.gui.manager.GuiManager;
import code.cache.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.print.attribute.standard.MediaName;

public class GuiListener implements Listener{

    private Manager manager;

    public GuiListener(Manager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onOpenGUI(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();

        UserCache userCache = manager.getCache().getPlayerUUID().get(player.getUniqueId());
        OnlineSample onlineSample = manager.getManagingCenter().getGuiManager().getSampleManager().getOnlineSample();

        if (!userCache.isGUISet()){
            return;
        }

        if (event.getClickedInventory() == null){
            return;
        }

        if (event.getCurrentItem() == null){
            return;
        }

        if (event.getCurrentItem().getType() == Material.AIR){
            return;
        }

        if (!event.getCurrentItem().hasItemMeta()){
            return;
        }

        manager.getPlugin().getLogger().info("Detecting..");
        if (userCache.equalsGUIGroup("online")){
                    manager.getPlugin().getLogger().info("true");
            onlineSample.getOnlineClickEvent(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){

        HumanEntity player = event.getPlayer();
        UserCache userCache = manager.getCache().getPlayerUUID().get(player.getUniqueId());

        if (userCache.isChangingPage()){
            return;
        }

        if (userCache.isGUISet()){
            userCache.setGUIGroup("default");
        }

        if (userCache.getPage() > 0){
            userCache.changePage(0);
        }
    }
}
