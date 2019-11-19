package dev.russia9.trainpix.listeners;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.modules.ListModule;
import dev.russia9.trainpix.modules.PhotoModule;
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
    private ListModule listModule;
    private PhotoModule photoModule;

    private LocaleManager localeManager;

    public MessageCreateListener(String clientID, LocaleManager localeManager) {
        this.clientID = clientID;
        this.localeManager = localeManager;

        listModule = new ListModule(localeManager);
        photoModule = new PhotoModule(localeManager);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (listModule.check(messageCreateEvent.getMessageContent())) {
            listModule.process(messageCreateEvent);
        }
        if (photoModule.check(messageCreateEvent.getMessageContent())) {
            photoModule.process(messageCreateEvent);
        }
    }
}
