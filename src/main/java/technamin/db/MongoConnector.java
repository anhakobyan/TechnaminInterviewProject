package technamin.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import technamin.services.Configuration;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoConnector extends MongoClient {

    private MongoConnector() {
        super(getServerAddress(), getAuths());
    }

    private static MongoConnector mongoConnector;

    public static MongoConnector getInstance() throws UnknownHostException {
        if (mongoConnector == null) {
            mongoConnector = new MongoConnector();
        }
        return mongoConnector;
    }

    private static ServerAddress getServerAddress(){
        return new ServerAddress(Configuration.DB_HOST, Configuration.DB_PORT);
    }

    private static List<MongoCredential> getAuths() {
        MongoCredential testAuth = MongoCredential.createScramSha1Credential(Configuration.DB_USER, Configuration.DB_SOURCE, Configuration.DB_PASS.toCharArray());
        List<MongoCredential> auths = new ArrayList<>();
        auths.add(testAuth);
        return auths;
    }

}
