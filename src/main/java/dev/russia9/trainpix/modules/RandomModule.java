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

import static dev.russia9.trainpix.lib.ParseHelper.getPage;

/**
 * /photo command module
 *
 * @author Russia9
 * @since 0.1
 */
public class RandomModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private final LocaleManager localeManager;
    private final String[] aliases = {
            "random",
            "rd"
    };

    public RandomModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void process(MessageCreateEvent event) {
        EmbedBuilder reply = new EmbedBuilder();

        String lang = "en";
        if (event.isServerMessage() && event.getServer().get().getRegion().getKey().equals("russia")) {
            lang = "ru";
        }
        logger.trace("Detected LANG:" + lang);

        try {
            String photoLink = "https://trainpix.org/ph.php";
            Document photo = getPage(photoLink, lang);
            String photoImage = "https://trainpix.org" + photo.getElementById("ph").attr("src");

            String trainLink = "https://trainpix.org/" + photo.getElementsByAttributeValueStarting("href", "/vehicle").get(0).attr("href");
            Document train = getPage(trainLink, lang);

            String trainName = train.getElementsByTag("h1").get(0).text();

            String authorName = photo.getElementsByClass("cmt_aname").get(0).getElementsByTag("a").text();

            // Build date detection
            Elements built = train.getElementsContainingOwnText(localeManager.getString(lang, "train.built"));
            String buildDate = localeManager.getString(lang, "train.built") + " " + localeManager.getString(lang, "unknown");

            if (!built.isEmpty() && built.parents().get(0).children().size() > 0) {
                buildDate = localeManager.getString(lang, "train.built") + " " + built.parents().get(0).getElementsByTag("b").text();
            }

            // Depot detection
            Elements depot = train.getElementsContainingOwnText(localeManager.getString(lang, "train.depot"));
            String depotName = localeManager.getString(lang, "train.depot") + " " + localeManager.getString(lang, "unknown");

            if (!depot.isEmpty() && depot.parents().get(0).children().size() > 0) {
                depotName = depot.parents().get(0).getElementsByTag("a").text();
            }

            // Road detection
            Elements road = train.getElementsContainingOwnText(localeManager.getString(lang, "train.road"));
            String roadName = localeManager.getString(lang, "train.road") + " " + localeManager.getString(lang, "unknown");

            if (!road.isEmpty() && road.parents().get(0).children().size() > 0) {
                roadName = road.parents().get(0).getElementsByTag("a").text();
            }

            // Category detection
            Elements category = train.getElementsContainingOwnText(localeManager.getString(lang, "train.category"));
            String categoryName = "Other";

            if (!category.isEmpty() && road.parents().get(0).children().size() > 0) {
                categoryName = category.parents().get(0).child(1).text();
            }

            // Condition and color detection
            Color color = new Color(220, 220, 220);
            Element state = photo.getElementsByClass("state").first();
            String stateText = state.text();
            if ("ru".equals(lang)) {
                if (stateText.contains("Новый")) {
                    color = new Color(108, 220, 53);
                }
                if (stateText.contains("Списан")) {
                    color = new Color(220, 122, 110);
                }
                if (stateText.contains("Не работает")) {
                    color = new Color(220, 217, 76);
                }
                if (stateText.contains("Нынешнее местонахождение и судьба неизвестны")) {
                    color = new Color(220, 138, 104);
                }
                if (stateText.contains("Памятник/музейный экспонат/тренажёр")) {
                    color = new Color(220, 53, 37);
                }
            } else {
                if (stateText.contains("New")) {
                    color = new Color(108, 220, 53);
                }
                if (stateText.contains("Written off")) {
                    color = new Color(220, 122, 110);
                }
                if (stateText.contains("Out of order")) {
                    color = new Color(220, 217, 76);
                }
                if (stateText.contains("Current location and condition are unknown")) {
                    color = new Color(220, 138, 104);
                }
                if (stateText.contains("Monument/Museum exhibit/Trainer")) {
                    color = new Color(220, 53, 37);
                }
            }

            reply.setTitle(trainName);
            reply.setAuthor(Reference.botName, trainLink, Reference.botImageLink);
            reply.addField(roadName + " | " + depotName, categoryName + " | " + stateText);
            reply.setImage(photoImage);
            reply.setColor(color);
            reply.setFooter(authorName + " | " + buildDate);
        } catch (IOException e) {
            logger.trace(e);
            reply.setAuthor(Reference.botName);
            reply.setTitle(localeManager.getString(lang, "errors.500.header"));
            reply.addField(localeManager.getString(lang, "errors.500.title"), localeManager.getString(lang, "errors.500.description"));
        } catch (IndexOutOfBoundsException e) {
            logger.trace(e);
            reply.setAuthor(Reference.botName);
            reply.setTitle(localeManager.getString(lang, "errors.404.header"));
            reply.addField(localeManager.getString(lang, "errors.404.title"), localeManager.getString(lang, "errors.404.description"));
        }

        event.getChannel().sendMessage(reply);
    }
}