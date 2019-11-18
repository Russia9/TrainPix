package dev.russia9.trainpix.modules;


import dev.russia9.trainpix.i18n.LocaleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * /list command module
 */
public class ListModule implements BotModule {
    private static final Logger logger = LogManager.getLogger(ListModule.class.getName());
    private LocaleManager localeManager;

    public ListModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        return message.contains("/list");
    }

    @Override
    public void process(MessageCreateEvent event) {
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) { // First page
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();

            try {
                String searchUrl = "https://trainpix.org/vsearch.php?cid=0&mid=0&state=0&serial_type=&works_number=&id_number=&anybuilt=1&anywritten=1&note=&info=&order=3&num=" + URLEncoder.encode(searchQuery, "UTF-8");
                String cookie = "en";

                if (event.getServer().get().getRegion().getKey().equals("russia")) {
                    cookie = "ru";
                }

                Document document = Jsoup
                        .connect(searchUrl)
                        .cookie("lang", cookie)
                        .get();

                Element table = document.getElementsByClass("main").get(0).getElementsByTag("table").get(0).child(0);

                int count = Integer.parseInt(document.getElementsByClass("main").get(0).getElementsByTag("p").get(0).getElementsByTag("b").get(0).text());
                int size = table.children().size() - 3;
                if (size > 15) size = 15;

                reply.setAuthor("TrainPix");
                reply.setTitle(searchQuery);
                reply.setFooter(localeManager.getString(cookie, "list.results") + size + "/" + count);

                for (int i = 2; i < size + 2; ++i) {
                    Element row = table.child(i);
                    StringBuilder head = new StringBuilder();
                    StringBuilder body = new StringBuilder();
                    if (!row.child(0).child(0).text().equals(""))
                        head.append("**").append(row.child(0).child(0).text()).append("**");
                    if (!row.child(4).text().equals("")) head.append(" | ").append(row.child(4).text());
                    if (!row.child(5).text().equals("")) head.append(" | ").append(row.child(5).text());
                    switch (row.className()) {
                        case "s1":
                        case "s11":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.working"));
                            break;
                        case "s2":
                        case "s12":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.new"));
                            break;
                        case "s3":
                        case "s13":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.broken"));
                            break;
                        case "s4":
                        case "s14":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.written_off"));
                            break;
                        case "s5":
                        case "s15":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.unknown"));
                            break;
                        case "s6":
                        case "s16":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.moved"));
                            break;
                        case "s7":
                        case "s17":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.krp"));
                            break;
                        case "s8":
                        case "s18":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.moved2"));
                            break;
                        case "s9":
                        case "s19":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.monument"));
                            break;
                        case "s10":
                        case "s20":
                            head.append(" | ").append(localeManager.getString(cookie, "train.status.refurbishment"));
                            break;
                    }

                    if (!row.child(2).text().equals(""))
                        body.append(localeManager.getString(cookie, "train.built")).append(row.child(2).text());
                    else body.append(localeManager.getString(cookie, "train.built.unknown"));
                    if (row.children().size() > 6) body.append(" | ").append(row.child(6).text());

                    reply.addField(head.toString(), body.toString());
                }

                event.getChannel().sendMessage(reply).get().addReaction("\uD83D\uDDD1\n");
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
