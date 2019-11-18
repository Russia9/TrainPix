package dev.russia9.trainpix.modules;

import org.javacord.api.event.message.MessageCreateEvent;

public interface BotModule {
    /**
     * Checking messages for relation to this module.
     * @param message message
     * @return true if related, false if no
     */
    public boolean check(String message);

    public void process(MessageCreateEvent event);
}
