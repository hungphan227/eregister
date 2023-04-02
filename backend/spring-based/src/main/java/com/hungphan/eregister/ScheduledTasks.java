package com.hungphan.eregister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedDelay = 5*1000)
    public void scheduleTaskWithFixedRate() {
        LOGGER.info("Send email to producers to inform quantity sold items");
    }

}
