package com.java.weddingplz;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Slf4j
public class WeddingMain {

    public static void main(String args[]) throws InterruptedException {
        String webPage = "https://www.weddingplz.com/delhi-ncr/photographers-and-videographers";
        int numberOfPages = 57;
        generateReport(webPage, numberOfPages);
    }

    private static void generateReport(String webPage, int numberOfPages) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<String> webPages = getWebPages(webPage, numberOfPages);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfPages);
        List<Future<List<Vendor>>> vendorFutures = webPages.stream().map(w -> executorService.submit(new PageVendorsJob(w))).collect(Collectors.toList());

        executorService.shutdown();

        executorService.awaitTermination(30, TimeUnit.MINUTES);

        List<Vendor> allVendors = new ArrayList<>();
        vendorFutures.forEach(f -> {
            try {
                allVendors.addAll(f.get());
            } catch (Exception e) {
                log.error("Exception while processing futures: {}", e);
            }
        });

        List<Vendor> sortedVendors = allVendors.stream().sorted(Comparator.comparing(v -> v.pinCode)).collect(Collectors.toList());
        CSVGenerator.writeDataLineByLine(getOutputFile(webPage) + "_withDuplicates.csv", sortedVendors);
        List<Vendor> deduped = allVendors.stream()
            .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Vendor::getName))),
                ArrayList::new));
        List<Vendor> sortedDeduped = deduped.stream().sorted(Comparator.comparing(v -> v.pinCode)).collect(Collectors.toList());
        CSVGenerator.writeDataLineByLine(getOutputFile(webPage) + ".csv", sortedDeduped);

        long totalTimeInMillis = (System.currentTimeMillis() - start);
        log.info("Finished processing file: {} in {} seconds. Total enteries: {}. Total enteries after dedup: {}", getOutputFile(webPage), (totalTimeInMillis / 1000) % 60, allVendors.size(),
            deduped.size());

    }

    private static String getOutputFile(String webPage) {
        return webPage.substring(webPage.lastIndexOf("/") + 1);
    }

    static List<String> getWebPages(String baseUrl, int numberOfPages) {
        return IntStream.range(0, numberOfPages).mapToObj(i -> baseUrl + "?page=" + (i + 1)).collect(Collectors.toList());
    }

}
