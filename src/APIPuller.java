
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.io.IOException;


public class APIPuller {
    public String getAccessToken() throws IOException, InterruptedException {
        String spotify_secret = "14a40f1ebfbc43d4a68b737567c950d7";
        String client_id = "0f94df5707024ee49ceb1144705cc75c";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .POST(BodyPublishers.ofString("grant_type=client_credentials&client_id="+client_id+"&client_secret="+spotify_secret))
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String data = response.body();
//        parse data for access token data is JSON
        String[] dataArr = data.split(",");
        String[] tokenArr = dataArr[0].split(":");
        String token = tokenArr[1].substring(1, tokenArr[1].length()-1);
        return token;
    }

    public String searchArtist(String artist) throws IOException, InterruptedException {
        String token = getAccessToken();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/search?q="+artist+"&type=artist"))
                .GET()
                .setHeader("Authorization", "Bearer "+token)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String data = response.body();
        return data;
    }

    public String getArtist(String artistID) throws IOException, InterruptedException {
        String token = getAccessToken();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists/"+artistID))
                .GET()
                .setHeader("Authorization", "Bearer "+token)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String data = response.body();
        return data;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        APIPuller api = new APIPuller();
        String uzi_id = "4O15NlyKLIASxsJ0PrXPfz";
        String data = api.getArtist(uzi_id);
        System.out.println(data);
    }
}
