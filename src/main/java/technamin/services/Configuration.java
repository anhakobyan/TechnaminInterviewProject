package technamin.services;

import org.apache.log4j.Logger;

import java.util.*;
import java.io.*;

public class Configuration {
    final static Logger logger = Logger.getLogger(Configuration.class);
    public static final String FILE_PATH;
    public static String QUEUE;
    public static String DB_HOST;
    public static Integer DB_PORT;
    public static String DB_USER;
    public static String DB_PASS;
    public static String DB_SOURCE;
    public static String DB_NAME;

    static {
        Properties p = new Properties();
        FileReader reader;
        try {
            reader = new FileReader("application.properties");
            try {
                p.load(reader);
            } catch (IOException e) {
                logger.error("unable to load properties", e);
            }
        } catch (FileNotFoundException e) {
            logger.error("wrong path for properties", e);
        }
        FILE_PATH = p.getProperty("file.path");
        QUEUE = p.getProperty("rabbitMQ.queue");
        DB_HOST = p.getProperty("db.host");
        DB_PORT = Integer.parseInt(p.getProperty("db.port"));
        DB_USER = p.getProperty("db.user");
        DB_PASS = p.getProperty("db.pass");
        DB_SOURCE = p.getProperty("db.source");
        DB_NAME = p.getProperty("db.name");
    }
}
