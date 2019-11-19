package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;

import static dev.russia9.trainpix.lib.Lib.getPage;

public class PhotoModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;

    public PhotoModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        logger.trace("Check `" + message + "`");
        return message.contains("/photo");
    }

    @Override
    public void process(MessageCreateEvent event) {
        logger.debug("Processing `" + event.getMessageContent() + "`");
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) { // First page
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();

            try {
                String searchUrl = "" + URLEncoder.encode(searchQuery, "UTF-8");
                String lang = "en";

                if (event.getServer().get().getRegion().getKey().equals("russia")) {
                    lang = "ru";
                }

                Document document = getPage(searchUrl, lang);

            } catch (IOException ignored) {

            }

            event.getChannel().sendMessage(reply);
        }
    }
}