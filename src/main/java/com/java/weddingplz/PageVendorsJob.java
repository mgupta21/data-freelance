package com.java.weddingplz;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class PageVendorsJob implements Callable<List<Vendor>> {

    private final String webpage;

    PageVendorsJob(String webPage) {
        this.webpage = webPage;
    }

    @Override
    public List<Vendor> call() throws Exception {

        log.info("Processing webpage --> {}", webpage);

        List<Vendor> vendors = new ArrayList<>();
        try {
            BufferedReader readr = reader(webpage);

            // read each line from stream till end
            Vendor.VendorBuilder vendorBuilder = Vendor.builder();
            String line;
            while ((line = readr.readLine()) != null) {
                if (line.contains("<a itemprop")) {
                    // System.out.println("line ---> " + line);
                    String myurl = line.split("<a itemprop=\"url\" href=\"")[1].split("\" title")[0];
                    String mytitle = line.split("title =")[1].trim().split(">")[0].replace("\"", "");

                    BufferedReader innnerReader = reader(myurl);

                    String line2;
                    boolean scanner = true;
                    while ((line2 = innnerReader.readLine()) != null && scanner) {
                        scanner = populateVendorObj(vendors, vendorBuilder, line, mytitle, line2, scanner);
                    }

                    innnerReader.close();
                }
                // writer.write(line);
            }

            readr.close();
            // writer.close();
            return vendors;

        } catch (Exception e) {
            System.out.println("Exception while processing : " + e);
        }
        return vendors;
    }

    private boolean populateVendorObj(List<Vendor> vendors, Vendor.VendorBuilder vendorBuilder, String line, String mytitle, String line2, boolean scanner) {
        if (line2.contains("smsMeForm")) {
            String myAddress = line2.split("vendorAddress=")[1].split("&vendorMno")[0];
            Integer myPincode = extractPincode(line, line2, myAddress);
            String myPhone = extractPhone(line2);
            String myEmail = line2.split("vendorEmail=")[1].split("&category_id")[0];
            // System.out.println("Title=" + mytitle + " & Url=" + myurl + " & Address=" + myAddress + " & Pin=" + myPincode + " & Phone=" + myPhone );
            vendors.add(vendorBuilder.name(mytitle.trim()).address(myAddress).phoneNumber(myPhone).pinCode(myPincode).email(myEmail).build());
            scanner = false;
        }
        return scanner;
    }

    private String extractPhone(String line2) {
        String str = line2.split("vendorMno=")[1].split("&vendorEmail")[0];
        return str.replaceAll(" ", "");
    }

    private static BufferedReader reader(String webpage) {
        try {
            // Create URL object
            URL url = new URL(webpage);
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            log.error("Exception while processing : {}", e);
        }
        return null;
    }

    private static Integer extractPincode(String line, String line2, String address) {
        Integer intPin = 0;
        address = address.trim();
        String addressSubstr;
        if (address.length() > 11) {
            addressSubstr = address.substring(address.length() - 10);
        } else {
            addressSubstr = address;
        }

        try {
            char wChar = '–';
            char hyp = '-';
            int lastIndex;
            if (addressSubstr.lastIndexOf(wChar) == -1) {
                lastIndex = addressSubstr.lastIndexOf(hyp);
            } else {
                lastIndex = addressSubstr.lastIndexOf(wChar);
            }

            if (lastIndex == -1) {
                lastIndex = addressSubstr.length() - 7;
            }
            String myPincode = addressSubstr.substring(lastIndex + 1);
            myPincode = myPincode.replaceAll(" ", "");
            intPin = Integer.valueOf(myPincode);
        } catch (Exception e) {
            log.error("Exception while processing pin. {}", e.getMessage());
            log.info("vendorlist={}", line);
            log.info("vendorAdr={}", address);
            log.info("vendorInfo={} \n", line2);
        }
        return intPin;
    }

    public static void main(String[] args) {
        String address = "1156-57/1190, Kucha Mahajani, Chandni Chowk, New Delhi-110006\u200E";
        Integer integer = extractPincode("", "", address);
        System.out.println();
    }

}
