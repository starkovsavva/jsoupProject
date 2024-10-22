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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Получаем текущую сессию
        HttpSession session = req.getSession(false); // Используем `false`, чтобы не создавать новую сессию, если её нет

        if (session != null) {
            // Получаем список топиков из сессии
            List<Topic> topics = (List<Topic>) session.getAttribute("topics");

            if (topics != null) {
                // Добавляем топики в атрибут запроса для передачи в JSP
                req.setAttribute("topics", topics);
            } else {
                // Если топиков нет, можно задать пустой список или сообщение
                req.setAttribute("message", "Топики пока не загружены.");
            }
        } else {
            // Если сессии нет, можно задать сообщение об ошибке
            req.setAttribute("message", "Сессия не найдена.");
        }

        // Перенаправляем на `topics.jsp` для отображения данных
        req.getRequestDispatcher("/topics.jsp").forward(req, resp);
    }
}