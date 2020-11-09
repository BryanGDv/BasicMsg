package code.commands;

import code.modules.SocialSpyMethod;
import code.registry.ConfigManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.bukkitutils.SoundManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import code.utils.Configuration;


public class SocialSpyCommand implements CommandClass{

    private final Manager manager;

    public SocialSpyCommand(Manager manager) {
        this.manager = manager;

    }

    @Command(names = {"socialspy", "spy"})
    public boolean onCommand(CommandSender sender, @OptArg String args, @OptArg OfflinePlayer target) {

        SocialSpyMethod socialspy = manager.getPlayerMethods().getSocialSpyMethod();
        ConfigManager files = manager.getFiles();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        SoundManager sound = manager.getSounds();

        Configuration config = files.getConfig();
        Configuration messages = files.getMessages();
        Configuration command = files.getCommand();


        if (!(sender instanceof Player)) {
            System.out.println(playersender.getMessage(messages.getString("error.console")));
            return true;
        }

        Player player = (Player) sender;

        if (!(manager.getPathManager().isCommandEnabled("socialspy"))){
            playersender.sendMessage(sender, messages.getString("error.command-disabled")
                    .replace("%player%", player.getName())
                    .replace("%command%", "socialspy"));
            return true;
        }

        if (!(sender.hasPermission(config.getString("config.perms.socialspy")))) {
            playersender.sendMessage(sender, messages.getString("error.no-perms"));
            sound.setSound(player.getUniqueId(), "sounds.error");
            return true;

        }

        if (args == null) {
            socialspy.toggle(player.getUniqueId());
            playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.toggle")
                    .replace("%mode%", socialspy.getStatus()));
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.toggle");
            return true;
        }

        if (args.equalsIgnoreCase("-list")){
            playersender.sendMessage(sender, command.getString("commands.socialspy.space"));

            if (socialspy.getVariable() == null) {
                playersender.sendMessage(sender, messages.getString("error.socialspy.anybody"));
            }else{
                if (socialspy.getVariable().isEmpty()){
                    playersender.sendMessage(sender, messages.getString("error.socialspy.anybody"));
                }else{
                    playersender.sendMessage(sender, command.getString("commands.socialspy.list-spyplayers"));
                    playersender.sendMessage(sender, "&8- &f" + socialspy.getVariable());
                }
            }
            playersender.sendMessage(player, command.getString("commands.socialspy.space"));
            sound.setSound(target.getPlayer().getUniqueId(), "sounds.on-list");
            return true;
        }
        if (args.equalsIgnoreCase("on")) {
            if (target == null) {

                if (socialspy.getVariable() == null){
                    socialspy.set(player.getUniqueId());
                    playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.enabled"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;

                }
                if (socialspy.getVariable().contains(player.getUniqueId())) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.activated"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }
                socialspy.set(player.getUniqueId());
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.enabled"));
            } else {
                String targetname = target.getName();
                if (!target.isOnline()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.player-offline"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }
                if (socialspy.getVariable().contains(target.getUniqueId())) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.arg-2-activated").replace("%arg-2%", targetname));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }
                socialspy.set(target.getUniqueId());
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.arg-2.enabled").replace("%arg-2%", target.getName()));
                playersender.sendMessage(target.getPlayer(), command.getString("commands.socialspy.player.enabled"));
            }
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.enable");
            return true;
        }

        if (args.equalsIgnoreCase("off")) {
            if (target == null){

                if (!(socialspy.getVariable().contains(player.getUniqueId()))) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.unactivated"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;

                }
                socialspy.unset(player.getUniqueId());
                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.player.disabled"));
            } else {
                String targetname = target.getName();

                if (!target.isOnline()) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.player-offline"));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }
                if (!(socialspy.getVariable().contains(target.getUniqueId()))) {
                    playersender.sendMessage(player.getPlayer(), messages.getString("error.socialspy.arg-2-unactivated").replace("%arg-2%", targetname));
                    sound.setSound(player.getUniqueId(), "sounds.error");
                    return true;
                }
                socialspy.unset(target.getUniqueId());

                playersender.sendMessage(player.getPlayer(), command.getString("commands.socialspy.arg-2.disabled").replace("%arg-2%", targetname));
                playersender.sendMessage(target.getPlayer(), command.getString("commands.socialspy.player.disabled"));
            }
            sound.setSound(player.getUniqueId(), "sounds.on-socialspy.disable");
            return true;
        } else {
            playersender.sendMessage(sender, messages.getString("error.unknown-arg"));
            playersender.sendMessage(sender, "&8- &fUsage: &a/socialspy [on/off] [player]");
            sound.setSound(player.getUniqueId(), "sounds.error");

        }

        return true;
    }

}
