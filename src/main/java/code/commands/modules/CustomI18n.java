package code.commands.modules;

import code.Manager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.translator.TranslationProvider;

import java.util.HashMap;
import java.util.Map;

public class CustomI18n implements TranslationProvider {

    protected Map<String, String> translations;

    public CustomI18n(Manager manager){
        translations = new HashMap<>();
        translations.put("command.subcommand.invalid", "1. The subcommand %s doesn't exists!");
        translations.put("command.no-permission", "2. No permission.");
        translations.put("argument.no-more","3. No more arguments were found, size: %s position: %s");
        translations.put("player.offline", "4. The player %s is offline!");
        translations.put("sender.unknown", "5. The sender for the command is unknown!");
        translations.put("sender.only-player", "6. Only players can execute this command!");
        manager.getLogs().log("Translator created!");
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

    @Override
    public String getTranslation(Namespace namespace, String key){
        return getTranslation(key);
    }
}