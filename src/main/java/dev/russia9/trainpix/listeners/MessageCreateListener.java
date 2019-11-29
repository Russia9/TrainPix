package dev.russia9.trainpix.listeners;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.modules.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;

/**
 * Message creation Listener
 *
 * @author Russia9
 * @since v0.0.1
 */
public class MessageCreateListener implements org.javacord.api.listener.message.MessageCreateListener {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private String clientID;
    private BotModule modules[];

    private LocaleManager localeManager;

    public MessageCreateListener(String clientID, LocaleManager localeManager) {
        this.clientID = clientID;
        this.localeManager = localeManager;

        modules = new BotModule[]{
                new ListModule(localeManager),
                new PhotoModule(localeManager),
                new HelpModule(localeManager),
        };
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        for (BotModule module : modules) {
            if (module.check(messageCreateEvent.getMessage().getContent())) {
                module.process(messageCreateEvent);
            }
        }
    }
}
