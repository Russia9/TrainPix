package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import org.javacord.api.event.message.MessageCreateEvent;

public class PhotoModule implements BotModule {
    private LocaleManager localeManager;

    public PhotoModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        return message.contains("/photo");
    }

    @Override
    public void process(MessageCreateEvent event) {

    }
}
