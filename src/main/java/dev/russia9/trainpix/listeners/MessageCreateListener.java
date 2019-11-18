package dev.russia9.trainpix.listeners;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * Message creation Listener
 *
 * @author Russia9
 * @since v0.0.1
 */
public class MessageCreateListener implements org.javacord.api.listener.message.MessageCreateListener {
    private String clientID;

    public MessageCreateListener(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

    }
}
