package com.github.vastap.core.loggers;

import com.github.vastap.core.beans.Event;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileEventLogger implements EventLogger {
    private String fileName;

    public FileEventLogger(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void logEvent(Event event) {
        try {
            File file = new File(fileName);
            FileUtils.writeStringToFile(file, event.toString()+"\n", Charset.forName("UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        File file = new File(fileName);
        if (!file.canWrite()) {
            throw new IllegalStateException("Can't write to file " + fileName);
        }
    }
}
