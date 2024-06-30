package org.httploadtest.lib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class HttpLoadTest {
    private final String url;
    private int numberOfRequests;
    private int numberOfConcurrent;

    public HttpLoadTest() {
        this.url = "";
        this.numberOfRequests = 1;
    }

    public HttpLoadTest(String url) {
        this.url = url;
    }

    public HttpLoadTest(String url, int numberOfRequests, int numberOfConcurrent) {
        this.url = url;
        this.numberOfRequests = numberOfRequests;
        this.numberOfConcurrent = numberOfConcurrent;
    }

    public Map<String, String> loadTest() throws InterruptedException, ExecutionException {
        ExecutorService executorService = null;

        try {
            Map<String, String> response = new HashMap<>();

            executorService = Executors.newFixedThreadPool(numberOfConcurrent);
            HttpClient client = HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_1_1).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            int numberOfSuccess = 0;
            int numberOfFailures = 0;

            List<CompletableFuture<Integer>> responses = new ArrayList<>();

            long startTime = System.currentTimeMillis();

            for(int i = 0; i < numberOfRequests; i++) {
                var req = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::statusCode);

                responses.add(req);
            }

            for(CompletableFuture<Integer> future : responses) {
                int status = future.get();

                if(status >= 200 && status <= 299) {
                    numberOfSuccess += 1;
                } else {
                    numberOfFailures += 1;
                }
            }

            long endTime = System.currentTimeMillis();
            long testTime = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));

            long requestsPerSecond = testTime <= 0 ? numberOfRequests : numberOfRequests / testTime;

            response.put("numberOfSuccess", String.valueOf(numberOfSuccess));
            response.put("numberOfFailures", String.valueOf(numberOfFailures));
            response.put("requestsPerSecond", String.valueOf(requestsPerSecond));

            return response;
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        } finally {
            if(executorService != null) {
                executorService.shutdown();
            }
        }
    }
}
