package com.github.vastap;

public class App {
    Client client;
    ConsoleEventLogger eventLogger;

    public static void main(String[] args) {
        App app = new App();

        app.client = new Client("1", "John Smith");
        app.eventLogger = new ConsoleEventLogger();

        app.logEvent("Hello World from user 1!");
    }

    public void logEvent(String msg) {
        String message = msg.replaceAll(client.getId(), client.getFullName());
        eventLogger.logEvent(message);
    }
}
