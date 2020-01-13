package dev.russia9.trainpix.lib;

public class Lib {
    public static String getSearchQuery(String[] message) {
        StringBuilder searchQuery = new StringBuilder();
        for (int i = 1; i < message.length; i++) {
            if (!message[i].equals(" ")) {
                searchQuery.append(message[i].trim()).append(" ");
            }
        }
        return searchQuery.toString().trim();
    }
}
