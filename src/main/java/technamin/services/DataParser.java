package technamin.services;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import technamin.data.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;


public class DataParser {

    private void readJsonData(String path) throws IOException {
        try (
                InputStream inputStream = Files.newInputStream(Path.of(path));
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            while (reader.hasNext()) {
                Data person = new Gson().fromJson(reader, Data.class);
                //write to mongo...
            }
            reader.endArray();
        }
    }

}
