package dev.russia9.trainpix.modules;


import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.lib.Lib;
import dev.russia9.trainpix.lib.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

import static dev.russia9.trainpix.lib.ParseHelper.getPage;

/**
 * /list command module
 *
 * @author Russia9
 * @since 0.1
 */
public class ListModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private final LocaleManager localeManager;
    private final String[] aliases = {
            "list",
            "l"
    };

    public ListModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void process(MessageCreateEvent event) {
        String[] message = event.getMessageContent().split(" ");
        if (message.length >= 2) { // First page
            String searchQuery = Lib.getSearchQuery(message);

            EmbedBuilder reply = new EmbedBuilder();

            String lang = "en";
            if (event.isServerMessage() && event.getServer().get().getRegion().getKey().equals("russia")) {
                lang = "ru";
            }
            logger.trace("Detected LANG: " + lang);

            try {
                String searchUrl = "https://trainpix.org/vsearch.php?&order=2&num=" + URLEncoder.encode(searchQuery, "UTF-8");
                Document document = getPage(searchUrl, lang);

                Elements trains = document.getElementsByClass("main").get(0)
                        .getElementsByTag("tbody").get(0)
                        .getElementsByAttributeValueStarting("class", "s");

                int count = Integer.parseInt(document.getElementsByClass("main").get(0).getElementsByTag("p").get(0).getElementsByTag("b").get(0).text());

                int i = 0;

                for (Element trainRow : trains) {
                    if (i >= Reference.maxListSize) {
                        break;
                    }
                    StringBuilder head = new StringBuilder();
                    StringBuilder body = new StringBuilder();

                    boolean process = false;

                    String trainUrl = "https://trainpix.org/" + trainRow.getElementsByTag("a").attr("href");
                    Document train = getPage(trainUrl, lang);

                    String trainName = train.getElementsByTag("h1").get(0).text();
                    logger.trace(trainName);

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

                    head.append(trainName);
                    head.append(" | ").append(roadName);

                    body.append(buildDate);
                    body.append(" | ").append(depotName);

                    switch (trainRow.className()) { // Checking state
                        case "s1":
                        case "s11":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.working"));
                            process = true;
                            break;
                        case "s2":
                        case "s12":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.new"));
                            process = true;
                            break;
                        case "s3":
                        case "s13":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.broken"));
                            process = true;
                            break;
                        case "s4":
                        case "s14":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.written_off"));
                            process = true;
                            break;
                        case "s5":
                        case "s15":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.unknown"));
                            process = true;
                            break;
                        case "s7":
                        case "s17":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.krp"));
                            process = true;
                            break;
                        case "s9":
                        case "s19":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.monument"));
                            process = true;
                            break;
                        case "s21":
                        case "s31":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.refurbishment"));
                            process = true;
                            break;
                        default:
                            break;
                    }

                    if (process) {
                        i++;
                        reply.addField(head.toString(), body.toString());
                    }
                }
                reply.setAuthor(Reference.botName);
                reply.setTitle(searchQuery);
                reply.setFooter(localeManager.getString(lang, "list.results") + i + "/" + count);
            } catch (IOException e) {
                logger.warn(e);
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
}
