package org.acme.examssb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ExamsSbApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamsSbApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ExamsSbApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LOGGER.info("Using timezone: {}", TimeZone.getDefault());
    }
}
