import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {
    static ObjectMapper mapper = new ObjectMapper();

    static CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                    .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                    .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                    .build())
            .build();

    public static void main(String[] args) {
        String api_key = "2FiybMWMmdSnTFFAYqGC2TULX3X5TuG2Zmgucv3j";
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + api_key;

        HttpGet dataRequest = new HttpGet(url);

        System.out.println("Устанавливаем соединение с сервером NASA");

        try (CloseableHttpResponse response = httpClient.execute(dataRequest);
             InputStream content = (response.getEntity().getContent())
        ) {
            System.out.println("Запрашиваем данные");

            // Преобразуем поток ответа в формате JSON в объект NasaData
            NasaData nasaData = mapper.readValue(content, NasaData.class);

            // Скачиваем и сохраняем файлы для полученного объекта
            saveNasaData(nasaData);

            System.out.println("Получение данных от NASA завершено");
        } catch (IOException e) {
            System.out.println("Возникла ошибка при получении данных от сервера NASA!");
        }
    }

    // Обёртка для сохранения файлов в разном качестве для объекта NasaData
    static void saveNasaData(NasaData nasaData) {
        String dirName = nasaData.getDate();

        String url = nasaData.getUrl();
        String[] urlSplit = url.split("/");

        String hdUrl = nasaData.getHdUrl();
        String[] hdUrlSplit = hdUrl.split("/");

        saveFileByUrl(url, dirName);
        saveFileByUrl(hdUrl, dirName);
    }

    // Сохранение файла, расположенного по <url>, в директорию <dirName>
    static void saveFileByUrl(String url, String dirName) {
        String[] urlSplit = url.split("/");
        String fileName = urlSplit[urlSplit.length - 1];

        // Создаём директорию
        File dir = new File(dirName);
        dir.mkdir();

        // Создаём файл
        File file = new File(dirName + "\\" + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Возникла ошибка при создании файла!");
        }

        HttpGet fileRequest = new HttpGet(url);

        // Читаем данные с сайта и сразу пишем их в файл
        try (CloseableHttpResponse fileResponse = httpClient.execute(fileRequest);
             InputStream inputStream = fileResponse.getEntity().getContent()
        ) {
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                for (int i = 0; i < fileResponse.getEntity().getContentLength() + 3; i++) {
                    outputStream.write(inputStream.read());
                }
                System.out.println("Сохранён файл: " + dirName + "\\" + fileName);
            } catch (IOException e) {
                System.out.println("Возникла ошибка при сохранении файла!");
            }
        } catch (IOException e) {
            System.out.println("Возникла ошибка при получении файла!");
        }
    }
}
