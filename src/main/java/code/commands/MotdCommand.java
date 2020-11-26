package code.commands;

import code.Manager;
import code.bukkitutils.other.PageManager;
import code.methods.player.PlayerMessage;
import code.utils.Configuration;
import code.utils.module.ModuleCheck;
import code.utils.StringFormat;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.*;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@Command(names = {"motd"})
@ArgOrSub
@Required

public class MotdCommand implements CommandClass {

    private final Manager manager;

    private final Configuration utils;
    private final Configuration command;
    private final Configuration messages;

    private final PlayerMessage sender;
    private final ModuleCheck moduleCheck;
    private final List<String> motd;


    public MotdCommand(Manager manager) {
        this.manager = manager;

        this.utils = manager.getFiles().getBasicUtils();
        this.command = manager.getFiles().getCommand();
        this.messages = manager.getFiles().getMessages();

        this.sender = manager.getPlayerMethods().getSender();
        this.moduleCheck = manager.getPathManager();
        this.motd = utils.getStringList("utils.join.motd.format");
    }

    @Command(names = "")
    // Por si preguntas del ("1") el hashmap comienza desde 0 :xd: asi que 0 es página 1.
    public boolean onCommand(@Sender Player player, int page) {

        StringFormat variable = manager.getVariables();

        if (!(moduleCheck.isCommandEnabled("motd"))) {
            moduleCheck.sendDisableMessage(player, "motd");
            return true;
        }

        if (page <= 0){
            sender.sendMessage(player, messages.getString("error.motd.negative-number")
                    .replace("%number%", String.valueOf(page)));
            return true;
        }

        PageManager pageManager = new PageManager(motd);
        pageManager.getHashString().get(0).forEach(System.out::println);
        Bukkit.broadcastMessage("page: " + page);


        if (!pageManager.pageExists(page - 1)){
            sender.sendMessage(player, messages.getString("error.motd.unknown-page")
                    .replace("%page%", String.valueOf(page)));
            return true;
        }

        List<String> motdpage = pageManager.getHashString().get(page - 1);
        List<String> motdlist = command.getStringList("commands.motd.list.message");

        motdlist.replaceAll(text -> text
                                .replace("%page%", String.valueOf(page))
                                .replace("%maxpage%", String.valueOf(pageManager.getMaxPage())));

        motdpage.replaceAll(text -> variable.replacePlayerVariables(player, text));

        motdlist.forEach(text -> sender.sendMessage(player, text));
        sender.sendMessage(player, command.getString("commands.motd.list.space"));
        motdpage.forEach(text -> sender.sendMessage(player, text));
        sender.sendMessage(player, command.getString("commands.motd.list.space"));

        return true;
    }

    @Command(names = {"addline"})
    public boolean addLine(@Sender Player player, @OptArg("") @Text String text) {

        if (text.isEmpty()){
            sender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "motd", "addline/removeline/setline");
            return true;
        }

        String message = String.join(" ", text);

        sender.sendMessage(player, command.getString("commands.motd.add-line")
                    .replace("%line%", message));

        motd.add(message);
        utils.set("utils.join.motd.format", motd);
        utils.save();
        return true;
    }

    @Command(names = {"removeline"})
    public boolean removeLine(@Sender Player player, @OptArg("1") int page) {

        String pageArg = String.valueOf(page);

        if (pageArg.isEmpty()) {
            sender.sendMessage(player, command.getString("commands.motd.remove-line")
                    .replace("%line%", motd.get(motd.size() - 1))
                    .replace("%number%", String.valueOf(motd.size())));
            motd.remove(motd.size() - 1);
        } else {

            String linepath;

            try {
                linepath = motd.get(page - 1);

            } catch (IndexOutOfBoundsException index) {
                sender.sendMessage(player, messages.getString("error.motd.unknown-line")
                        .replace("%line%", String.valueOf(page)));
                return true;

            }

            sender.sendMessage(player, command.getString("commands.motd.remove-line")
                    .replace("%line%", linepath)
                    .replace("%number%", String.valueOf(page)));
            motd.remove(page - 1);
        }


        utils.set("utils.join.motd.format", motd);
        utils.save();
        return true;
    }

    @Command(names = {"setline"})
    public boolean setLine(@Sender Player player, int page, @OptArg("") @Text String text) {

        if (text.isEmpty()){
            sender.sendMessage(player, messages.getString("error.no-arg"));
            moduleCheck.getUsage(player, "motd", "addline/removeline/setline");
            return true;
        }

        String message = String.join("", text);

        String linepath;

        try {
            linepath = motd.get(page - 1);

        } catch (IndexOutOfBoundsException index) {
            sender.sendMessage(player, messages.getString("error.motd.unknown-line")
                    .replace("%line%", String.valueOf(page)));
            return true;

        }
        motd.set(page - 1, message);

        sender.sendMessage(player, command.getString("commands.motd.set-line")
                .replace("%beforeline%", linepath)
                .replace("%line%", motd.get(page - 1))
                .replace("%number%", String.valueOf(page)));

        utils.set("utils.join.motd.format", motd);
        utils.save();
        return true;
    }

}
