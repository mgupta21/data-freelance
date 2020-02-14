package com.here.liveroads.mapUpdateVerifier.report;

import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {

    FileWriter writer;

    ReportWriter(String fileName) {
        try {
            writer = new FileWriter(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String text) {
        try {
            writer.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBlankLine() {
        writeToFile("\n");
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
