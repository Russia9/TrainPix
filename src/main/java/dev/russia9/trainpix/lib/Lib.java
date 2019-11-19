package dev.russia9.trainpix.lib;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Lib {
    public static Document getPage(String url, String lang) throws IOException {
        return Jsoup
                .connect(url)
                .cookie("lang", lang)
                .get();
    }
}
