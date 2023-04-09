package com.hungphan.eregister;

import com.hungphan.eregister.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private CourseService courseService;

    @Scheduled(fixedDelay = 60*30000)
    public void scheduleTaskWithFixedRate() {
        LOGGER.info("check Pending RestApi Calls");
        courseService.checkPendingRestApiCalls();
    }

}
