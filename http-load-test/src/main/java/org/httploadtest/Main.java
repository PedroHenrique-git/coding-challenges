package org.httploadtest;

import org.httploadtest.lib.HttpLoadTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<String> listOfArgs = List.of(args);

        if(listOfArgs.isEmpty()) {
            System.out.println("Invalid parameters");

            return;
        }

        if(!listOfArgs.contains("-u")) {
            System.out.println("Invalid url");

            return;
        }

        String url = listOfArgs.get(listOfArgs.indexOf("-u") + 1);
        int numberOfRequests = listOfArgs.contains("-n") ? Integer.parseInt(listOfArgs.get(listOfArgs.indexOf("-n") + 1)) : 1;
        int numberOfConcurrent = listOfArgs.contains("-c") ? Integer.parseInt(listOfArgs.get(listOfArgs.indexOf("-c") + 1)) : 1;

        HttpLoadTest httpLoadTest = new HttpLoadTest(url, numberOfRequests, numberOfConcurrent);

        Map<String, String> result = httpLoadTest.loadTest();

        if(result.containsKey("error")) {
            System.out.println("error: " + result.get("error"));

            return;
        }

        String output = "Results: \n" +
                String.format("\tTotal Requests (2XX).......................: %s \n", result.get("numberOfSuccess")) +
                String.format("\tFailed Requests (5XX)......................: %s \n", result.get("numberOfFailures")) +
                String.format("\tRequests/second ......................: %s \n", result.get("requestsPerSecond"));

        System.out.println(output);
    }
}