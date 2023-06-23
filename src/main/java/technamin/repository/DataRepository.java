package technamin.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.model.Updates;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.apache.log4j.Logger;
import technamin.data.Data;
import technamin.db.MongoConnector;
import technamin.services.Configuration;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;


public class DataRepository {

    private static DataRepository dataRepository;
    private final MongoCollection<Data> mongoCollection;
    final static Logger logger = Logger.getLogger(DataRepository.class);

    private DataRepository() {
        try {
            MongoConnector mongoConnector = MongoConnector.getInstance();
            CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );
            mongoCollection = mongoConnector.getDatabase(Configuration.DB_NAME).withCodecRegistry(codecRegistry).getCollection("data", Data.class);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataRepository getInstance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    public Optional<JsonObject> saveOrUpdate(Data data) {
        Data oldData = this.mongoCollection.find(eq("doc_id", data.getDoc_id())).first();
        JsonObject updateMetadata = new JsonObject();
        if (oldData == null) {
            mongoCollection.insertOne(data);
            updateMetadata.addProperty("doc_id", data.getDoc_id());
            updateMetadata.addProperty("update_type", "Save");
            logger.info("data for doc_id:" + data.getDoc_id() + " saved");
        } else {
            List<Bson> updates = new ArrayList<>();
            updateMetadata.addProperty("doc_id", data.getDoc_id());
            updateMetadata.addProperty("update_type", "Update");
            JsonArray updatedFields = new JsonArray();
            if (!oldData.getSeq().equals(data.getSeq())) {
                updates.add(Updates.set("seq", data.getSeq()));
                updatedFields.add("seq");
            }
            if (!oldData.getData().equals(data.getData())) {
                updates.add(Updates.set("data", data.getData()));
                updatedFields.add("data");
            }
            if (!oldData.getTime().equals(data.getTime())) {
                updates.add(Updates.set("time", data.getTime()));
                updatedFields.add("time");
            }
            if (!updatedFields.isEmpty()) {
                mongoCollection.updateOne(eq("doc_id", data.getDoc_id()), Updates.combine(updates));
                updateMetadata.add("fields", updatedFields);
                logger.info("data for doc_id:" + data.getDoc_id() + " updated");
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(updateMetadata);
    }
}
