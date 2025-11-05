import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080; // porta do servidor
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Rota principal (serve o index.html)
        server.createContext("/", new FileHandler("web/index.html", "text/html"));

        // Rota para o CSS
        server.createContext("/style.css", new FileHandler("web/style.css", "text/css"));

        System.out.println("Servidor rodando em http://localhost:" + port);
        server.start();
    }

    // Classe auxiliar para servir arquivos estáticos
    static class FileHandler implements HttpHandler {
        private final String filePath;
        private final String contentType;

        public FileHandler(String filePath, String contentType) {
            this.filePath = filePath;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Path path = Path.of(filePath);
            byte[] bytes = Files.readAllBytes(path);
            exchange.getResponseHeaders().add("Content-Type", contentType + "; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}

