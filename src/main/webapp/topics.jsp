<%@ page import="com.tictactoe.example.model.Topic" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="refresh" content="5">
    <meta charset="UTF-8">
    <title>Auto Update Topics</title>
</head>
<body>
<h1>Topics</h1>

<%
    List<Topic> topics = (List<com.tictactoe.example.model.Topic>) request.getAttribute("topics");
    String message = (String) request.getAttribute("message");
%>

<% if (topics != null && !topics.isEmpty()) { %>
<div id="topics">
    <% for (com.tictactoe.example.model.Topic topic : topics) { %>
    <p>
        Название: <%= topic.getName() %><br>
        Описание: <%= topic.getInfo() %><br>
        Дата создания: <%= topic.getDate() %><br>
        Размер: <%= topic.getSize() %>
    </p>
    <% } %>
</div>
<% } else if (message != null) { %>
<p><%= message %></p>
<% } else { %>
<p>Топики пока не загружены.</p>
<% } %>

</body>
</html>
