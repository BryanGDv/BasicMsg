package code.utils;

import code.modules.player.PlayerStatic;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;


public class HoverManager{

    private final TextComponent textComponent;

    public HoverManager(String string) {
        textComponent = new TextComponent(string);
    }

    public TextComponent getTextComponent(){
        return textComponent;
    }

    public void setHover(String string){
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(string).create()));
    }

    public String hoverMessage(Player player, List<String> string){
        String path = String.join("\n", string);
        String pathcolored = PlayerStatic.setColor(path)
                .replace("%player%", player.getName())
                .replace("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlayerStatic.setVariables(player, pathcolored);
        }else{
            return pathcolored;
        }
    }

}
