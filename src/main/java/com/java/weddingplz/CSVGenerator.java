package com.java.weddingplz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

public class CSVGenerator {

    public static void writeDataLineByLine(String filePath, List<Vendor> vendorList) {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter

            CSVWriter writer = new CSVWriter(outputfile, ';',
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

            // adding header to csv
            String[] header = {"Address", "Pin Code"};
            writer.writeNext(header);

            vendorList.forEach(v -> writer.writeNext(new String[] {v.profile(), String.valueOf(v.pinCode)}));

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
