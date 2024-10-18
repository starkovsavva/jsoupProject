import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@WebServlet("/getTopics")
public class GetTopicsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Здесь вы можете запустить асинхронный парсинг топиков
        CompletionStage<String> parsingResult = parseTopicsAsync();

        // Когда результат будет готов, отправляем его клиенту
        parsingResult.thenAccept(result -> {
            try {
                resp.getWriter().write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private CompletionStage<String> parseTopicsAsync() {
        // Здесь вы можете запустить асинхронный парсинг топиков
        // Например, используя CompletableFuture
        return CompletableFuture.supplyAsync(() -> {
            // Симуляция парсинга
            try {
                Thread.sleep(1000); // Имитация задержки
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "[{"topicId": "123", "content": "Parsed content 1"}, {"topicId": "456", "content": "Parsed content 2"}]";
        });
    }
}