package technamin.processor;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import technamin.data.Data;
import technamin.repository.DataRepository;
import org.json.simple.JSONObject;
import technamin.services.RabbitMQSender;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Processor {
    final static Logger logger = Logger.getLogger(Processor.class);

    public static void startTheProcess(String path) {
        try {
            ExecutorService executorService =
                    new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>());
            DataRepository dataRepository = DataRepository.getInstance();
            JSONParser parser = new JSONParser();
            JSONArray inputArray = (JSONArray) parser.parse(new FileReader(path));

            List<Data> dataList = new ArrayList<>();
            for (Object inputObject : inputArray) {
                Data item = new Data();

                JSONObject obj = (JSONObject) inputObject;
                item.setDoc_id((Long) obj.get("doc_id"));
                item.setSeq((Long) obj.get("seq"));
                item.setData((String) obj.get("data"));
                item.setTime((Long) obj.get("time"));

                dataList.add(item);
            }

            Map<Long, List<Data>> dataListGrouped =
                    dataList.stream().collect(Collectors.groupingBy(Data::getDoc_id));
            dataListGrouped.forEach((doc_id, groupedList) ->
                    executorService.submit(() -> groupedList.forEach(b -> {
                        Optional<JsonObject> updateInfo = dataRepository.saveOrUpdate(b);
                        if (updateInfo.isPresent()) {
                            try {
                                logger.info("write to rabbitMQ: metadata " + updateInfo);
                                RabbitMQSender.send(updateInfo.get());
                            } catch (Exception e) {
                                logger.error("Unable send data to rabbitMQ", e);
                            }
                        }
                    })));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
