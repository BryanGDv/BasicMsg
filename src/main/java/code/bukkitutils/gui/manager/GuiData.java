package code.bukkitutils.gui.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class GuiData {

    private final Inventory inv;

    public GuiData(String title, int size){
        inv = Bukkit.createInventory(null, size, title);
    }

    public boolean containsItems(){
        return inv.getContents() != null;
    }

    public String getTitle(){
        return inv.getTitle();
    }

    public void addItem(String item, String name, List<String> lore) {
        inv.addItem(createItem(Material.valueOf(item.toUpperCase()), name, lore));
    }

    public void addItem(String item, String name) {
        inv.addItem(createItem(Material.valueOf(item.toUpperCase()), name));
    }


    public void addHead(String owner, String name, List<String> lore){
        inv.addItem(createHead(owner, name, lore));
    }

    public ItemStack createHead(String owner, String name, List<String> lore){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        SkullMeta itemMeta = (SkullMeta) skull.getItemMeta();

        itemMeta.setOwner(owner);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        skull.setItemMeta(itemMeta);

        return skull;
    }

    public void setItem(int id, String item, String name, List<String> lore){
        if (lore != null) {
            inv.setItem(id, createItem(Material.valueOf(item.toUpperCase()), name, lore));
        } else {
            inv.setItem(id, createItem(Material.valueOf(item.toUpperCase()), name));
        }
    }


    public void reset(){
        inv.clear();
    }

    public ItemStack createItem(Material material, String name){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItem(Material material, String name, List<String> lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public Inventory getInv(){
        return inv;
    }
}
