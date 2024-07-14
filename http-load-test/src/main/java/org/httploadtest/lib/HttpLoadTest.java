package org.httploadtest.lib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.*;

public class HttpLoadTest {
    private final String url;
    private final int numberOfRequests;
    private final int numberOfConcurrent;

    public HttpLoadTest() {
        this.url = "";
        this.numberOfRequests = 1;
        this.numberOfConcurrent = 1;
    }

    public HttpLoadTest(String url) {
        this.url = url;
        this.numberOfRequests = 1;
        this.numberOfConcurrent = 1;
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

            List<CompletableFuture<Long[]>> responses = new ArrayList<>();

            long startTime = System.currentTimeMillis();

            for(int i = 0; i < numberOfRequests; i++) {
                long ttfb = System.currentTimeMillis();

                var req = client
                        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(resp -> {
                            long ttlb = System.currentTimeMillis();
                            int statusCode = resp.statusCode();

                            return new Long[]{(long) statusCode, ttfb, ttlb};
                        });

                responses.add(req);
            }

            List<Long> allRequestTTFB = new ArrayList<>();
            List<Long> allRequestTTLB = new ArrayList<>();

            for(CompletableFuture<Long[]> future : responses) {
                Long[] reqInfo = future.get();

                long status = reqInfo[0];
                long ttfb = reqInfo[1];
                long ttlb = reqInfo[2];

                allRequestTTFB.add(ttfb);
                allRequestTTLB.add(ttlb);

                if(status >= 200 && status <= 299) {
                    numberOfSuccess += 1;
                } else {
                    numberOfFailures += 1;
                }
            }

            long endTime = System.currentTimeMillis();
            long testTime = TimeUnit.MILLISECONDS.toSeconds((endTime - startTime));

            long requestsPerSecond = testTime <= 0 ? numberOfRequests : numberOfRequests / testTime;

            long minTTFB = allRequestTTFB.stream().min(Long::compareTo).orElse(0L);
            long maxTTFB = allRequestTTFB.stream().max(Long::compareTo).orElse(0L);
            double meanTTFB = allRequestTTFB.stream().mapToDouble(n -> n).average().orElse(0);

            long minTTLB = allRequestTTLB.stream().min(Long::compareTo).orElse(0L);
            long maxTTLB = allRequestTTLB.stream().max(Long::compareTo).orElse(0L);
            double meanTTLB = allRequestTTLB.stream().mapToDouble(n -> n).average().orElse(0);

            response.put("numberOfSuccess", String.valueOf(numberOfSuccess));
            response.put("numberOfFailures", String.valueOf(numberOfFailures));
            response.put("requestsPerSecond", String.valueOf(requestsPerSecond));

            response.put("minTime", String.valueOf(minTTLB));
            response.put("maxTime", String.valueOf(maxTTLB));
            response.put("meanTime", String.valueOf(meanTTLB));

            response.put("minTTFB", String.valueOf(minTTFB));
            response.put("maxTTFB", String.valueOf(maxTTFB));
            response.put("meanTTFB", String.valueOf(meanTTFB));

            response.put("minTTLB", String.valueOf(minTTLB));
            response.put("maxTTLB", String.valueOf(maxTTLB));
            response.put("meanTTLB", String.valueOf(meanTTLB));

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
