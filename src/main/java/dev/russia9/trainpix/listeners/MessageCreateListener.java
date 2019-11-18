package dev.russia9.trainpix.listeners;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.modules.ListModule;
import org.javacord.api.event.message.MessageCreateEvent;

/**
 * Message creation Listener
 *
 * @author Russia9
 * @since v0.0.1
 */
public class MessageCreateListener implements org.javacord.api.listener.message.MessageCreateListener {
    private String clientID;
    private ListModule listModule;

    private LocaleManager localeManager;

    public MessageCreateListener(String clientID, LocaleManager localeManager) {
        this.clientID = clientID;
        this.localeManager = localeManager;

        listModule = new ListModule(localeManager);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(listModule.check(messageCreateEvent.getMessageContent())) {
            listModule.process(messageCreateEvent);
        }
    }
}
