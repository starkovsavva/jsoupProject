package com.tictactoe.example.model;
import javax.imageio.ImageIO;

import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class Topic {
    public URI link;
    public String image;
    public String name;//
    public String info; //post-b
    public LocalDateTime date;
    public String size;
    Topic(URI link, String name, LocalDateTime date, String size){
        this.link = link;
        this.name = name;
        this.date = date;
        this.size = size;
        parseTopic();

    }

    @SneakyThrows
    private void parseTopic(){
        Document d = Jsoup.connect(link.toString()).get();
        Element ElemImageURL = d.select("var.postImg.postImgAligned.img-right").first();

        if(ElemImageURL != null){
            this.image = ElemImageURL.attr("title");
//            System.out.println(this.image);

        }

        Element postbElems = d.select("div.post_wrap").first();
        if(postbElems != null){
            this.info = postbElems.text();

        }
//        System.out.println(postbElems.text());
    }


}
