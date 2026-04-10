package com.task.manage.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OnAppStart {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws Exception {

        System.out.println("\n\n\n\n");
        System.out.println("-------------- " + ANSI_GREEN + "Spring Boot Application Started!" + ANSI_RESET + " --------------");
        System.out.println("\n\n");
    }
}
