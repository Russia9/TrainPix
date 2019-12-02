package dev.russia9.trainpix.modules;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * Interface for designing bot modules
 *
 * @author Russia9
 * @since 0.1
 */
public interface BotModule {
    /**
     * Get module command aliases
     *
     * @return String[] aliases
     */
    String[] getAliases();

    /**
     * Process event for this module
     *
     * @param event event
     */
    void process(MessageCreateEvent event);
}
