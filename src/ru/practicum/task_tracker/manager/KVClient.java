package manager;

import exception.ManagerSaveException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.StreamTokenizer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private static final String URL = "http://localhost:8078/";
    private final String apiToken;
    private final HttpClient httpClient;

    public KVClient() {
        httpClient = HttpClient.newHttpClient();
//        httpClient = HttpClient.newBuilder().build();
        apiToken = register();
    }

    public static void main(String[] args) {
        KVClient client = new KVClient();
//        client.register();
    }

    public String load (String key){ //грузить
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse <String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404){ //подкорректировать ___ если приложение будет падать
                System.out.println("Error");
                return null;
            }

            if (response.statusCode() != 200){
                throw new RuntimeException("Плохой ответ, не 200, а :" + response.statusCode()); //подкорректировать
//                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            //Конкретизировать что не так
            throw new RuntimeException("Не получается сделать запрос"); //подкорректировать
//            throw new ManagerSaveException("Can't do save request", e);
        }
    }

    public void put(String key, String value){ //метод чтобы ходить в KVServer // подкорректировать
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value)) //кладём наше значение
                    .build();
//            HttpResponse <String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse <Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding()); //??? ничего не возвращаем

            if (response.statusCode() != 200){
                throw new RuntimeException("Плохой ответ, не 200, а :" + response.statusCode()); //подкорректировать
//                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            //Конкретизировать что не так
            throw new RuntimeException("Не получается сделать запрос"); //подкорректировать
//            throw new ManagerSaveException("Can't do save request", e);
        }
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(URL + "register"))
//                    .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                    .build();
            HttpResponse <String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200){
                throw new RuntimeException("Плохой ответ, не 200, а :" + response.statusCode()); //подкорректировать
//                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается сделать запрос"); //подкорректировать
//            throw new ManagerSaveException("Can't do save request", e);
        }
    }
}
