package code.bukkitutils.other;

import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.entity.Player;

import java.util.*;

public class PageManager {

    private Map<Integer, List<String>> hashString;

    public PageManager(List<String> stringList){
        hashString = new HashMap<>();

        int count = 0;
        int pagenumber = 0;

        List<String> page = new ArrayList<>();

        for (String string : stringList){
            if (count >= 9){
                hashString.put(pagenumber, page);
                page = new ArrayList<>();

                pagenumber++;
                count = 0;
            }

            page.add(string);
            count++;
        }

        if (count > 0){
            hashString.put(pagenumber, page);
        }
    }

    public Map<Integer, List<String>> getHashString(){
        return hashString;
    }

    public Integer getMaxPage(){
        return hashString.size();
    }

    public boolean pageExists(Integer page) {
        return hashString.get(page) != null;
    }
}
