package dev.russia9.trainpix;

import dev.russia9.trainpix.listeners.MessageCreateListener;
import dev.russia9.trainpix.listeners.ReactionAddListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

/**
 * Class for managing bot listeners
 *
 * @author Russia9
 * @since v0.0.1
 */
public class Manager {
    private static final Logger logger = LogManager.getLogger(Manager.class.getName());
    private String token;

    public Manager(String token) {
        this.token = token;

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        api.addReactionAddListener(new ReactionAddListener());
        api.addMessageCreateListener(new MessageCreateListener());
    }
}
