package com.github.vastap.core;

import com.github.vastap.core.beans.Client;
import com.github.vastap.core.beans.Event;
import com.github.vastap.core.loggers.EventLogger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    private Client client;
    private Event event;
    private EventLogger eventLogger;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        App app = (App) context.getBean("app");
        for (int i = 0; i < 5; i++) {
            app.logEvent("Hello World from user 1!");
        }
        // Instead of context.close() use the hook:
        context.registerShutdownHook();
    }

    public App(Client client, EventLogger eventLogger, Event event) {
        this.client = client;
        this.eventLogger = eventLogger;
        this.event = event;
    }

    public void logEvent(String msg) {
        String message = msg.replaceAll(client.getId(), client.getFullName());
        event.setMsg(message);
        eventLogger.logEvent(event);
    }

}
