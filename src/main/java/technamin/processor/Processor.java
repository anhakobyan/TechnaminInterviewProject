package technamin.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;
import technamin.data.Data;
import technamin.repository.DataRepository;
import technamin.services.RabbitMQSender;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Processor {
    final static Logger logger = Logger.getLogger(Processor.class);

    public static void startTheProcess(String path) {
        try {
            DataRepository dataRepository = DataRepository.getInstance();

            ExecutorService executorService =
                    new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>());

            try (
                    InputStream inputStream = Files.newInputStream(Path.of(path));
                    JsonReader reader = new JsonReader(new InputStreamReader(inputStream))
            ) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                        (JsonDeserializer<Date>) (jsonElement, type, context) ->
                                new Date(jsonElement.getAsJsonPrimitive().getAsLong())).create();
                reader.beginArray();
                while (reader.hasNext()) {
                    Data data = gson.fromJson(reader, Data.class);
                    logger.info("read from the file: data " + data);
                    executorService.submit(() -> {
                        logger.info("write to mongoDb: data " + data);
                        Optional<JsonObject> updateInfo = dataRepository.saveOrUpdate(data);
                        if (updateInfo.isPresent()) {
                            try {
                                logger.info("write to rabbitMQ: metadata " + updateInfo);
                                RabbitMQSender.send(updateInfo.get());
                            } catch (Exception e) {
                                logger.error("Unable send data to rabbitMQ", e);
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
