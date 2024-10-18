package com.tictactoe.example.model;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import lombok.SneakyThrows;
import org.jsoup.select.Elements;

import javax.print.Doc;

@Data
public class Category {

    private String Title;
    private Element element;

    private boolean nextPageFlag = true;
    private String link;
    private Set<Topic> set = new HashSet<>();
    public Category(String link) {
        this.link = link;
        //title parse
        titleCategoryInit(link);
        parseTopic(link);

    }

    private Document document;

    public Category(Element element) {

        this.element = element;
        this.link = element.toString();


    }

    @SneakyThrows
    private void parseTopic(String link) {
        String curr = link;
        while(nextPageFlag){
            parseTopicInCategory(curr);
            String newHref = parseHref(curr);
            if(newHref.equals("false")){
                return;
            }
            curr = newHref;


        }


    }




    @SneakyThrows
    private String parseHref(String href) {
        Document d = Jsoup.connect(href).get();

        Element ElementNewHref = d.select("a.pg:contains(След.)").last();

        if(!(ElementNewHref != null)){
            System.out.println("Ссылка не сменилась нашлось : ссылка null" );
            this.nextPageFlag = false;
            return "false";
        }
        System.out.println("Ссылка сменилась на :" + ElementNewHref.absUrl("href"));
        return ElementNewHref.absUrl("href");

    };


    @SneakyThrows
    public void parseTopicInCategory(String href) {
        Document d = Jsoup.connect(href).get();
        Elements trTopics = d.select("tr.hl-tr");
        for (Element trTopic : trTopics) {

            Element trElement = trTopic.selectFirst("a.topictitle");
            String trTopicHref = trElement.absUrl("href");
            if (filterLink(trTopicHref)) {


                String trTopicTitle = trElement.text();

//        DATE CREATION
                Element trDateElem = trTopic.select("td.tCenter.small.nowrap p").get(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                LocalDateTime dateTime = LocalDateTime.parse(trDateElem.text(), formatter);


//            SIZE GB
                Element trSize = trTopic.select("td.tCenter.med.nowrap").first();
                String size = trSize.text();


                Topic topic = new Topic(URI.create(trTopicHref), trTopicTitle, dateTime, size);
                set.add(topic);
            }

        }

    }

    @SneakyThrows
    public void titleCategoryInit(String initHref){
        System.out.println(initHref);
        Document d = Jsoup.connect(initHref).get();
//        main title parse
        Element elem = d.select("h1.maintitle a").first();
        if (elem != null) {
            setElement(elem);
            setTitle(elem.text());
            System.out.println(Title);
        }
    }


    public boolean filterLink(String href) {
        // Извлекаем последние символы из ссылки
        String[] excludedEnd = new String[]{"4521", "95", "79", "23", "78", "1894", "63"};


        String lastChars = href.substring(href.lastIndexOf('=') + 1);
        // Проверяем, не совпадает ли последнее значение с исключаемым
        for (String s : excludedEnd) {
            if (lastChars.equals(s)) {
                return false;
            }
        }


        return true;
    }
}