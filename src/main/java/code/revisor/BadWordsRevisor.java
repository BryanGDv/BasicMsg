package code.revisor;

import code.Manager;
import code.bukkitutils.RunnableManager;
import code.modules.player.PlayerMessage;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class BadWordsRevisor {

    private Manager manager;

    private static Configuration utils;
    private static PlayerMessage playersender;
    private static RunnableManager runnableManager;

    public BadWordsRevisor(Manager manager) {
        this.manager = manager;
        utils = manager.getFiles().getBasicUtils();
        playersender = manager.getPlayerMethods().getSender();
    }

    public static String revisor(Player player, String string) {
        if (!(utils.getBoolean("utils.chat.security.bad-words.enabled"))) {
            return string;
        }

        List<String> badwordslist = utils.getStringList("utils.chat.security.bad-words.list-words");

        int words = 0;
        boolean bwstatus = false;

        for (String badword : badwordslist) {
            if (string.contains(badword)) {
                if (words < 1) {
                    playersender.sendMessage(player, utils.getString("utils.chat.security.bad-words.message")
                            .replace("%player%", player.getName()));

                    if (utils.getBoolean("utils.chat.security.bad-words.command.enabled")){
                        runnableManager.sendCommand(Bukkit.getConsoleSender(), utils.getString("utils.chat.security.bad-words.command.format")
                                .replace("%player%", player.getName()));
                    }
                    bwstatus = true;

                }
                words++;
            }

            string = string.replace(badword, utils.getString("utils.chat.security.bad-words.word-replaced"));
        }

        if (bwstatus) {
            if (utils.getBoolean("utils.chat.security.bad-words.word-list.enabled")) {
                playersender.sendMessage(player, utils.getString("utils.chat.security.bad-words.word-list.format")
                        .replace("%words%", String.valueOf(words)));
            }
        }

        return string;
    }
}
