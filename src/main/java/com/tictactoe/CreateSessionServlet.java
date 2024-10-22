package com.tictactoe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tictactoe.example.LocalDateTimeAdapter;
import com.tictactoe.example.Parser;
import com.tictactoe.example.model.Category;
import com.tictactoe.example.model.Topic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@WebServlet("/createSession")
public class CreateSessionServlet extends HttpServlet {
    private List<Topic> topicsAsync = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        Parser parser = new Parser();

        parser.parseCategoriesAsync().thenApply(aVoid -> parser.getTormac().getCategories().stream()
                .map(Category::parseTopicAsync)
                .collect(Collectors.toList())).thenAccept(futures ->
        {
            futures.forEach(topicFuture ->
                    topicFuture.thenAccept(topic -> {
                        topicsAsync.addAll(topic);
                        session.setAttribute("topics", topicsAsync);
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    })
            );
        });



        // Перенаправляем на страницу topics.jsp
        resp.sendRedirect("/getTopics");
    }
}
