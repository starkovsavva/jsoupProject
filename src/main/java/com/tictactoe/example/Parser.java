package com.tictactoe.example;

import lombok.Getter;
import lombok.SneakyThrows;
import com.tictactoe.example.model.Category;
import com.tictactoe.example.model.Tormac;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Parser {

    private static final Logger logger = LogManager.getLogger(Parser.class);

    private String link = "https://tormac.org/";
    @Getter
    private Tormac tormac = new Tormac();

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public Parser(String link) {
        this.link = link;
    }

    public Parser() {
    }

    @SneakyThrows
    public CompletableFuture<Void> parseCategoriesAsync() {
        logger.info("Начало асинхронного парсинга категорий с ссылки: {}", link);
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document d = Jsoup.connect(link).get();
                logger.info("Успешно загружен документ главной страницы.");
                System.out.println("Получило документ");
                Elements elements = d.getElementsByClass("forumlink");
                return elements;
            } catch (IOException e) {
                logger.error("Ошибка при подключении к ссылке: {}", link, e);
                throw new RuntimeException(e);
            }
        }, executor).thenAccept(elements -> {
            List<CompletableFuture<Void>> futures = elements.stream()
                    .filter(e -> !e.children().isEmpty())
                    .map(e -> e.child(0).absUrl("href"))
                    .filter(this::filter)
                    .map(detailsLink -> parseCategoryAsync(detailsLink))
                    .collect(Collectors.toList());

            logger.info("Найдено {} категорий для парсинга.", futures.size());
            // Ждем завершения всех задач парсинга категорий
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            logger.info("Асинхронный парсинг всех категорий завершен.");
        });
    }

    private CompletableFuture<Void> parseCategoryAsync(String detailsLink) {
        return CompletableFuture.runAsync(() -> {
            logger.info("Начало парсинга категории по ссылке: {}", detailsLink);
            try {
                Document dd = Jsoup.connect(detailsLink).get();
                Category category = new Category(detailsLink);
                tormac.getCategories().add(category);

                logger.info("Категория успешно добавлена: {}", detailsLink);
            } catch (IOException e) {
                logger.error("Ошибка при парсинге категории по ссылке: {}", detailsLink, e);
            }
        }, executor);
    }

    private boolean filter(String link) {
        if (link == null || link.isEmpty()) {
            logger.warn("Обнаружена пустая или null ссылка, пропуск фильтрации.");
            return false;
        }
        char lastdigit = link.charAt(link.length() - 1);
        boolean isValid = lastdigit >= '2' && lastdigit <= '9' && link.charAt(link.length() - 2) == '=';
        logger.debug("Результат фильтрации ссылки {}: {}", link, isValid);
        return isValid;
    }

    public void shutdown() {
        logger.info("Завершение работы парсера и остановка пула потоков.");
        executor.shutdown();
    }
}
