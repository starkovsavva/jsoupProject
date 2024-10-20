package com.tictactoe.example.model;

import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Data
public class Category {
    private String title;
    private boolean nextPageFlag = true;
    private String link;
    private ArrayList<Topic> set = new ArrayList<>();

    public Category(String link) {
        this.link = link;
        titleCategoryInit(link);

    }

    @SneakyThrows
    public CompletableFuture<ArrayList<Topic>> parseTopicAsync() {
        return CompletableFuture.supplyAsync(() -> {
            String curr = link;
            while (nextPageFlag) {
                try {
                    parseTopicInCategory(curr);
                    String newHref = parseHref(curr);
                    if (newHref.equals("false")) {
                        nextPageFlag = false;
                    } else {
                        curr = newHref;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return set;
        });
    }

    @SneakyThrows
    private String parseHref(String href) {
        Document d = Jsoup.connect(href).get();
        Element elementNewHref = d.select("a.pg:contains(След.)").last();

        if (elementNewHref == null) {
            this.nextPageFlag = false;
            return "false";
        }
        return elementNewHref.absUrl("href");
    }

    @SneakyThrows
    public void parseTopicInCategory(String href) {
        Document d = Jsoup.connect(href).get();
        Elements trTopics = d.select("tr.hl-tr");
        for (Element trTopic : trTopics) {
            Element trElement = trTopic.selectFirst("a.topictitle");
            String trTopicHref = trElement.absUrl("href");
            if (filterLink(trTopicHref)) {
                String trTopicTitle = trElement.text();
                Element trDateElem = trTopic.select("td.tCenter.small.nowrap p").get(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(trDateElem.text(), formatter);
                Element trSize = trTopic.select("td.tCenter.med.nowrap").first();
                String size = trSize.text();
                Topic topic = new Topic(URI.create(trTopicHref), trTopicTitle, dateTime, size);

                set.add(topic);
            }
        }
    }

    @SneakyThrows
    public void titleCategoryInit(String initHref) {
        Document d = Jsoup.connect(initHref).get();
        Element elem = d.select("h1.maintitle a").first();
        if (elem != null) {
            this.title = elem.text();
        }
    }

    public boolean filterLink(String href) {
        String[] excludedEnd = new String[]{"4521", "95", "79", "23", "78", "1894", "63"};
        String lastChars = href.substring(href.lastIndexOf('=') + 1);
        for (String s : excludedEnd) {
            if (lastChars.equals(s)) {
                return false;
            }
        }
        return true;
    }
}
