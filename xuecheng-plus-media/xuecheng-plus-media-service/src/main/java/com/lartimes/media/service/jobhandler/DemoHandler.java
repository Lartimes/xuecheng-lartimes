package com.lartimes.media.service.jobhandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/24 12:56
 */
@Component
@Slf4j
public class DemoHandler {


    @XxlJob("media_demo_handler")
    public void hello(){
        System.out.println("hello world");
        log.debug("media_demo_handler 执行");
    }
}
