package technamin;

import org.apache.log4j.Logger;
import technamin.processor.Processor;
import technamin.repository.DataRepository;
import technamin.services.Configuration;

public class Main {
    final static Logger logger = Logger.getLogger(DataRepository.class);

    public static void main(String[] args) {
        String path = args.length != 0 ? args[0] : Configuration.FILE_PATH;
        logger.info("input file path is: " + path);
        Processor.startTheProcess(path);
    }
}