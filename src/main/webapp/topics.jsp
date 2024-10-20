<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Auto Update Topics</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h1>Auto Update Topics</h1>
<div id="topics">
    <c:forEach var="topic" items="${sessionScope.topics}">
        <p>
            Название: ${topic.name} <br>
            Описание: ${topic.info} <br>
            Дата создания: ${topic.date} <br>
            Размер: ${topic.size} <br>
        </p>
    </c:forEach>
</div>

<script>
    $(document).ready(function() {
        function updateTopics() {
            $.ajax({
                url: 'getTopics',
                type: 'GET',
                success: function(response) {
                    var topics = JSON.parse(response);
                    $('#topics').empty();
                    topics.forEach(function(topic) {
                        $('#topics').append(
                            '<p>' +
                            'Название: ' + topic.name + '<br>' +
                            'Описание: ' + topic.info + '<br>' +
                            'Дата создания: ' + topic.date + '<br>' +
                            'Размер: ' + topic.size +
                            '</p>'
                        );
                    });
                },
                complete: function() {
                    setTimeout(updateTopics, 1000); // Обновляем раз в секунду
                }
            });
        }

        updateTopics();
    });
</script>
</body>
</html>
