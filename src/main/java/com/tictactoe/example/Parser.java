package com.tictactoe.example;
import lombok.SneakyThrows;
import com.tictactoe.example.model.Category;
import com.tictactoe.example.model.Tormac;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private String link = "https://tormac.org/";

    private Tormac tormac = new Tormac();

    Parser(String link){
        this.link = link;
    }

    public Parser() {

    }


    @SneakyThrows
    public void parseCategories(){
        Document d = Jsoup.connect(link).get();
        Elements elements = d.getElementsByClass("forumlink");
        for (Element e : elements){
            if(!e.children().isEmpty()){
                String detailsLink =  e.child(0).absUrl("href");
                if(filter(detailsLink)){
                    Document dd = Jsoup.connect(detailsLink).get();
                    Category category = new Category(detailsLink);
                    tormac.categories.add(category);
                }
            }
            //https://tormac.org/viewforum.php?f=2








        }
    }


    public boolean filter(String link){

        if (link == null || link.isEmpty()){
            return false;
        }

        char lastdigit = link.charAt(link.length() -1);

//        level = true;
        return lastdigit >= '2' && lastdigit <= '9' && link.charAt(link.length() -2 ) == '=';



    }


    public int lastDigitParse(String link){
        return link.charAt(link.length() -1);
    }

}
