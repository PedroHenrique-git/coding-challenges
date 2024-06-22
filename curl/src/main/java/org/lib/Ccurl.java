package org.lib;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ccurl {
    public static Map<String, String> makeRequest(String method, String url, String[] headers, String body) throws IOException, InterruptedException {
        Map<String, String> response = new HashMap<>();

        HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
        HttpRequest httpRequest = null;

        var httpRequestBuilder = HttpRequest.newBuilder(URI.create(url));

        if(headers.length > 0) {
            httpRequestBuilder.headers(headers);
        }

        if(method.equals("GET")) {
            httpRequestBuilder.GET();
        }

        if(method.equals("POST")) {
            httpRequestBuilder.POST(BodyPublishers.ofString(body));
        }

        if(method.equals("PUT")) {
            httpRequestBuilder.PUT(BodyPublishers.ofString(body));
        }

        if(method.equals("DELETE")) {
            httpRequestBuilder.DELETE();
        }

        httpRequest = httpRequestBuilder.build();

        var result = httpClient.send(httpRequest, BodyHandlers.ofString());

        String requestHost = "";
        String requestPath = "";
        String requestMethod = "";
        String requestHeadersStr = "";

        requestHost = result.request().uri().getHost();
        requestPath = result.request().uri().getPath();
        requestMethod = result.request().method();

        var requestHeaders = result.request().headers();

        if(requestHeaders != null) {
            requestHeadersStr = Ccurl.buildHeadersString(requestHeaders.map());
        }

        String responseBody = "";
        String responseHeadersStr = "";
        int responseStatus = 0;

        responseBody = result.body();
        responseStatus = result.statusCode();

        var responseHeaders = result.headers();

        if(responseHeaders != null) {
            responseHeadersStr = Ccurl.buildHeadersString(responseHeaders.map());
        }

        response.put("requestHost", requestHost);
        response.put("requestPath", requestPath);
        response.put("requestMethod", requestMethod);
        response.put("requestHeaders", requestHeadersStr);

        response.put("responseStatus", String.valueOf(responseStatus));
        response.put("responseStatusMessage", Ccurl.getStatusMessage(responseStatus));
        response.put("responseHeaders", responseHeadersStr);
        response.put("responseDate", Date.from(Instant.now()).toString());
        response.put("responseBody", responseBody);

        response.put("httpVersion", "1.1");

        return response;
    }

    private static String buildHeadersString(Map<String, List<String>> headers) {
        StringBuilder builder = new StringBuilder();

        headers.forEach((key, value) -> {
            builder.append(
                    String.format(
                            "%s: %s\n",
                            key,
                            value
                                    .toString()
                                    .replace("[", "")
                                    .replace("]", "")
                    )
            );
        });

        return builder.toString();
    }

    private static String getStatusMessage(int status) {
        if(status >= 500)
            return "INTERNAL SERVER ERROR";

        if(status >= 400)
            return "BAD REQUEST";

        if(status >= 300)
            return "REDIRECTION";

        if(status >= 200)
            return  "OK";

        if(status >= 100)
            return "INFO";

        return "OTHER";
    }
}
