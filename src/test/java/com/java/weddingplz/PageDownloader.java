package com.java.weddingplz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class PageDownloader {

    public static void main(String[] args) throws IOException {
        String myurl = "https://www.weddingplz.com/delhi-ncr/slimming-beauty-and-cosmetology-clinic?page=13";
        String filename = "downloaded-file.xml";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        try {

            // Create URL object
            URL url = new URL(myurl);
            BufferedReader readr = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = readr.readLine()) != null) {
            	printContent(line);

                writer.write(line);
            }

            readr.close();
            writer.close();
            System.out.println("Successfully Downloaded.");
        }

        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        } catch (IOException ie) {
            System.out.println("IOException raised");
        }
    }

    public static void printContent(String line) throws IOException {
        if (line.contains("<a itemprop")) {
            System.out.println("line ---> " + line);
            String myurl = line.split("<a itemprop=\"url\" href=\"")[1].split("\" title")[0];
            String mytitle = line.split("title =")[1].trim().split(">")[0].replace("\"", "");

            BufferedReader reader = reader(myurl);

            String line2;
            boolean scanner = true;
            while ((line2 = reader.readLine()) != null && scanner) {
                if (line2.contains("smsMeForm")) {
					System.out.println("line 2 ---> " + line2);
                    String myAddress = line2.split("vendorAddress=")[1].split("&vendorMno")[0];
                    String myPincode = myAddress.substring(myAddress.lastIndexOf("-") + 1);
                    String myPhone = line2.split("vendorMno=")[1].split("&vendorEmail")[0];
                    String myEmail = line2.split("vendorEmail=")[1].split("&category_id")[0];

                    System.out.println("Title=" + mytitle + " & Url=" + myurl + " & Address=" + myAddress + " & Pin=" + myPincode + " & Phone=" + myPhone + "& Email=" + myEmail);
                    scanner = false;
                }
            }
            // "<%=(.*?)%>"
        }
    }

    public static BufferedReader reader(String webpage) {
        try {

            // Create URL object
            URL url = new URL(webpage);
            BufferedReader readr =
                new BufferedReader(new InputStreamReader(url.openStream()));
            return readr;

        } catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        } catch (IOException ie) {
            System.out.println("IOException raised");
        }
        return null;
    }

}