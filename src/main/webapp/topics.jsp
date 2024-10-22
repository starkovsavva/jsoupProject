<%@ page import="com.tictactoe.example.model.Topic" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.swing.text.DateFormatter" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="refresh" content="5">
    <meta charset="UTF-8">
    <title>Auto Update Topics</title>
    <style>
        /* Добавляем стили */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }

        h1 {
            color: #333;
        }

        #search-container {
            margin-bottom: 20px;
        }

        input[type="text"] {
            padding: 8px;
            width: 300px;
        }

        #topics {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }

        .topic {
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 15px;
            width: 300px;
        }

        .topic img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
        }

        .topic h2 {
            font-size: 18px;
            color: #333;
            margin: 10px 0;
        }

        .topic p {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .download-link {
            display: inline-block;
            background-color: #007bff;
            color: #fff;
            padding: 5px 10px;
            text-decoration: none;
            border-radius: 5px;
        }

        .download-link:hover {
            background-color: #0056b3;
        }
    </style>
    <script>
        function searchTopics() {
            const query = document.getElementById("search-input").value.toLowerCase();
            const topics = document.querySelectorAll(".topic");

            topics.forEach(topic => {
                const name = topic.querySelector(".name").textContent.toLowerCase();
                if (name.includes(query)) {
                    topic.style.display = "block";
                } else {
                    topic.style.display = "none";
                }
            });
        }
    </script>
</head>
<body>
<h1>Tormac Parser</h1>

<div id="search-container">
    <input type="text" id="search-input" oninput="searchTopics()" placeholder="Search topics...">
</div>

<%
    List<Topic> topics = (List<com.tictactoe.example.model.Topic>) request.getAttribute("topics");
    String message = (String) request.getAttribute("message");
%>

<% if (topics != null && !topics.isEmpty()) { %>
<div id="topics">
    <% for (com.tictactoe.example.model.Topic topic : topics) { %>
    <div class="topic">
        <img src="<%= topic.getImage() %>" alt="Image for <%= topic.getName() %>">
        <h2 class="name"><%= topic.getName() %></h2>
        <p>
            <%
                String info = topic.getInfo();
                int descriptionIndex = info.toLowerCase().indexOf("описание:");
                if (descriptionIndex != 0) {
                    if (descriptionIndex > 0) {
                        // Если "Описание:" есть, но не в начале, перемещаем его в начало
                        info = "Описание: " + info.substring(descriptionIndex + "Описание:".length()).trim();
                    } else {
                        // Если "Описание:" вообще нет, добавляем в начало
                        info = "Описание: " + info;
                    }
                }
            %>
            <%= info.length() > 100 ? info.substring(0, 100) + "..." : info %>
        </p>
        <p>Дата создания: <%= topic.getDate() %></p>
        <p>Размер: <%= topic.getSize() %></p>
        <a class="download-link" href="<%= topic.getLink() %>" download>Скачать</a>
    </div>
    <% } %>
</div>
<% } else if (message != null) { %>
<p><%= message %></p>
<% } else { %>
<p>Топики пока не загружены.</p>
<% } %>

</body>
</html>
