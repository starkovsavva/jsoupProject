package com.tictactoe.example;
import com.tictactoe.example.model.Category;
import com.tictactoe.example.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.parseCategoriesAsync().thenRun(() -> {
            System.out.println("Парсинг категорий завершен. Найдено категорий: " + parser.getTormac().getCategories().size());

            // После завершения парсинга категорий, запускаем асинхронный парсинг топиков
            List<CompletableFuture<ArrayList<Topic>>> topicFutures = parser.getTormac().getCategories().stream()
                    .map(Category::parseTopicAsync)  // Запускаем асинхронный парсинг топиков для каждой категории
                    .collect(Collectors.toList());

            // Ждем завершения всех задач по парсингу топиков
            CompletableFuture.allOf(topicFutures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> {
                        System.out.println("Парсинг топиков завершен.");

                        // Вы можете добавить логику для обработки спарсенных топиков, если требуется

                        int i = 0;
                        System.out.println("парсинг топиков раз :" + i);
                        // Завершаем работу парсера
                        parser.shutdown();
                    })
                    .exceptionally(ex -> {
                        System.err.println("Произошла ошибка при парсинге топиков: " + ex.getMessage());
                        parser.shutdown();
                        return null;
                    });
        }).exceptionally(ex -> {
            System.err.println("Произошла ошибка при парсинге категорий: " + ex.getMessage());
            parser.shutdown();
            return null;
        });
    }



}
