<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Auto Update Topics</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h1>Auto Update Topics</h1>
<div id="topics"></div>

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
                        $('#topics').append('<p>Topic ID: ' + topic.topicId + ', Content: ' + topic.content + '</p>');
                    });
                },
                complete: function() {
                    // Запрашиваем обновления каждые 5 секунд
                    setTimeout(updateTopics, 5000);
                }
            });
        }

        // Начинаем обновление при загрузке страницы
        updateTopics();
    });
</script>
</body>
</html>