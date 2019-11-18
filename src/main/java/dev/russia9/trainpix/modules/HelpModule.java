package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import org.javacord.api.event.message.MessageCreateEvent;

public class HelpModule implements BotModule {
    private LocaleManager localeManager;

    public HelpModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        return message.contains("/help");
    }

    @Override
    public void process(MessageCreateEvent event) {

    }
}
