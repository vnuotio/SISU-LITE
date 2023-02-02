package fi.tuni.prog3.sisu.utility;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

//TODO: ERROR HANDLING, LIKE AT ALL??
/**
 * A class for handling HTTP request mainly from Sisu API. An instance 
 *  of this class can be used to send multiple different asynchronous 
 *  requests.
 */
public class HttpRequestHandler {
    private final HttpClient client;
    private HttpRequest request;
    
    /**
     * Initializes a new HttpClient.
     * @see java.net.http.HttpClient
     */
    public HttpRequestHandler()
    {
        client = HttpClient.newHttpClient();
    }
    
    /**
     * Gets the content from a HTTP request at the given URL as String.
     * @param url the full address for the request to get content from
     * @return received response as String
     * @see java.net.http.HttpResponse
     */
    public String getHttpContent(String url)
    {
        request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();
    }
}
