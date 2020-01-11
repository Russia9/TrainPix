package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.lib.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;

import static dev.russia9.trainpix.lib.ParseHelper.getPage;

/**
 * /photo command module
 *
 * @author Russia9
 * @since 0.1
 */
public class TrainModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;
    private String[] aliases = {
            "train",
            "t"
    };

    public TrainModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void process(MessageCreateEvent event) {
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) {
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();

            String lang = "en";
            if (event.isServerMessage() && event.getServer().get().getRegion().getKey().equals("russia")) {
                lang = "ru";
            }
            logger.trace("Detected LANG:" + lang);

            try {
                String searchUrl = "https://trainpix.org/search.php?cid=0&did=0&mid=0&place1=&place2=&place3=&notes=&konk=0&cammod=&aid=-1&auth=0&anydate=1&anypub=1&order=3&num=" + URLEncoder.encode(searchQuery, "UTF-8");
                Document document = getPage(searchUrl, lang);

            } catch (IOException e) {
                logger.trace(e);

                reply.setAuthor(Reference.botName);
                reply.setTitle(localeManager.getString(lang, "errors.500.header"));
                reply.addField(localeManager.getString(lang, "errors.500.title"), localeManager.getString(lang, "errors.500.description"));
            }

            event.getChannel().sendMessage(reply);
        }
    }
}