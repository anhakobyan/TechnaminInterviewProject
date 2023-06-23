package technamin.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoConnector extends MongoClient {

    private MongoConnector() throws UnknownHostException {
        super(getServerAddress(), getAuths());
    }

    private static MongoConnector mongoConnector;

    public static MongoConnector getInstance() throws UnknownHostException {
        if (mongoConnector == null) {
            mongoConnector = new MongoConnector();
        }
        return mongoConnector;
    }

    private static ServerAddress getServerAddress() throws UnknownHostException {
        return new ServerAddress("localhost", 27018);
    }

    private static List<MongoCredential> getAuths() {
        MongoCredential testAuth = MongoCredential.createScramSha1Credential("root", "admin", "root".toCharArray());
        List<MongoCredential> auths = new ArrayList<>();
        auths.add(testAuth);
        return auths;
    }

}
