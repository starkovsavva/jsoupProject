package com.tictactoe;

import com.google.gson.Gson;
import com.tictactoe.example.Parser;
import com.tictactoe.example.model.Category;
import com.tictactoe.example.model.Topic;
import com.tictactoe.example.model.Tormac;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@WebServlet("/getTopics")
public class GetTopicsServlet extends HttpServlet {

    private List<Topic> topicsAsync = Collections.synchronizedList(new ArrayList<>());
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        Parser parser = new Parser();

        // Асинхронно парсим категории
        parser.parseCategoriesAsync().thenApply(aVoid -> {
            // Создаем `CompletableFuture<Topic>` для каждой категории

            return parser.getTormac().getCategories().stream()
                    .map(Category::parseTopicAsync) // Предполагается, что этот метод возвращает `CompletableFuture<Topic>`
                    .collect(Collectors.toList());
        }).thenAccept(futures -> {
            futures.forEach(topicFuture ->
                topicFuture.thenAccept(topic -> {
                    // Добавляем новый топик в коллекцию
                    topicsAsync.addAll(topic);
                    session.setAttribute("topics", topicsAsync);

                    // Преобразуем топик в JSON
                    String json = new Gson().toJson(topic);

                    // Отправляем каждый топик по мере его парсинга
                    try {
                        resp.getWriter().write(json);
                        resp.flushBuffer(); // Отправляем ответ сразу, без ожидания завершения
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                })
            );
        });
    }
}