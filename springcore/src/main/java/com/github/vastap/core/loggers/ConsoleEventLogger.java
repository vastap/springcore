package com.github.vastap.core.loggers;

import com.github.vastap.core.beans.Event;

public class ConsoleEventLogger implements EventLogger {

    public void logEvent(Event event) {
        System.out.println(event);
    }
}
