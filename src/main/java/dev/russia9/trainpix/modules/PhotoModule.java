package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
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

import static dev.russia9.trainpix.lib.Lib.getPage;

public class PhotoModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;
    private String[] aliases = {
            "photo",
            "p"
    };

    public PhotoModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        logger.trace("Check `" + message + "` for " + this.getClass().getName());
        for (String alias : aliases) {
            if (message.contains(alias)) return true;
        }
        return false;
    }

    @Override
    public void process(MessageCreateEvent event) {
        logger.debug(this.getClass().getName() + " Processing `" + event.getMessageContent() + "`");
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) { // First page
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();

            try {
                String searchUrl = "https://trainpix.org/search.php?cid=0&did=0&mid=0&place1=&place2=&place3=&notes=&konk=0&cammod=&aid=-1&auth=0&anydate=1&anypub=1&order=3&num=" + URLEncoder.encode(searchQuery, "UTF-8");

                String lang = "en";
                if (event.isServerMessage()) {
                    if (event.getServer().get().getRegion().getKey().equals("russia")) {
                        lang = "ru";
                    }
                }

                Document document = getPage(searchUrl, lang);
                Elements photos = document.getElementsByClass("x");
                if (photos.size() > 0) {
                    Element photo = photos.get(0);
                    String photoPageLink = "https://trainpix.org" + photo.child(0).attr("href");

                    Document photoPage = getPage(photoPageLink, lang);
                    String photoLink = "https://trainpix.org" + photoPage.getElementById("ph").attr("src");
                    String authorName = photoPage.getElementsByClass("cmt_aname").get(0).getElementsByTag("a").text();
                    String authorLink = "https://trainpix.org" + photoPage.getElementsByClass("cmt_aname").get(0).getElementsByTag("a").attr("href");
                    String trainLink = "https://trainpix.org" + photoPage.getElementsByClass("pwrite").get(0).child(0).attr("href");
                    Document trainPage = getPage(trainLink, lang);

                    // Build date detection
                    Elements built = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.built"));
                    String buildDate = localeManager.getString(lang, "train.built.unknown");
                    if (built.parents().get(0).children().size() > 0) {
                        buildDate = localeManager.getString(lang, "train.built") + " " + built.parents().get(0).getElementsByTag("b").text();
                    }


                    // Depot detection
                    Elements depot = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.depot"));
                    String depotName = localeManager.getString(lang, "train.depot.unknown");
                    if (built.parents().get(0).children().size() > 0) {
                        depotName = depot.parents().get(0).getElementsByTag("a").text();
                    }

                    // Road detection
                    Elements road = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.road"));
                    String roadName = localeManager.getString(lang, "train.road.unknown");
                    if (road.parents().get(0).children().size() > 0) {
                        roadName = road.parents().get(0).getElementsByTag("a").text();
                    }

                    // Category detection
                    Elements category = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.category"));
                    String categoryName = "Other";
                    if (road.parents().get(0).children().size() > 0) {
                        categoryName = road.parents().get(0).child(1).text();
                    }

                    // Condition and color detection
                    Color color = new Color(220, 220, 220);
                    String condition = "Unknown";
                    Element state = photoPage.getElementsByClass("state").first();
                    String stateText = state.text();
                    if (lang.equals("ru")) {
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

                    reply.setAuthor(searchQuery);
                    reply.addField(roadName + " | " + depotName, categoryName + " | " + stateText);
                    reply.setImage(photoLink);
                    reply.setColor(color);
                    reply.setFooter(authorName);
                } else { // 404
                    reply.setAuthor("TrainPix");
                    reply.setTitle("404 Error");
                    reply.addField("Nothing found!", "Try other query");
                }
            } catch (IOException ignored) {

            }

            event.getChannel().sendMessage(reply);
        }
    }
}