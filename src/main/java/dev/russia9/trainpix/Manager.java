package dev.russia9.trainpix;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.listeners.MessageCreateListener;
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
    private static final Logger logger = LogManager.getLogger("TrainPix");

    private String token;
    private String clientID;

    private LocaleManager localeManager;

    public Manager(String token) {
        this.token = token;

        localeManager = new LocaleManager();

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        this.clientID = api.getClientId() + "";

        api.addMessageCreateListener(new MessageCreateListener(clientID, localeManager));
    }
}
