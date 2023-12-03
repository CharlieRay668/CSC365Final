
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.io.IOException;


public class APIPuller {
    private HttpClient client;
    private String spotify_secret;
    private String client_id;
    public APIPuller() {
        this.spotify_secret = "14a40f1ebfbc43d4a68b737567c950d7";
        this.client_id = "0f94df5707024ee49ceb1144705cc75c";
        this.client = HttpClient.newHttpClient();
    }

    public String makePostRequest(String url, String body, String header) throws IOException, InterruptedException {
        String[] headerArr = header.split(":");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(BodyPublishers.ofString(body))
                .setHeader(headerArr[0], headerArr[1])
                .build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        String data = response.body();
        return data;
    }

    public String makeGetReqeust(String url, String header) throws IOException, InterruptedException {
        String[] headerArr = header.split(":");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .setHeader(headerArr[0], headerArr[1])
                .build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        String data = response.body();
        return data;
    }

    public String getAccessToken() throws IOException, InterruptedException {
        String url = "https://accounts.spotify.com/api/token";
        String body = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+spotify_secret;
        String header = "Content-Type:application/x-www-form-urlencoded";
        String data = makePostRequest(url, body, header);
//        parse data for access token data is JSON
        String[] dataArr = data.split(",");
        String[] tokenArr = dataArr[0].split(":");
        String token = tokenArr[1].substring(1, tokenArr[1].length()-1);
        return token;
    }

    public String searchArtist(String artist) throws IOException, InterruptedException {
        String token = getAccessToken();
        String url = "https://api.spotify.com/v1/search?q="+artist+"&type=artist";
        String header = "Authorization:Bearer "+token;
        String data = makeGetReqeust(url, header);
        return data;
    }

    public String getArtist(String artistID) throws IOException, InterruptedException {
        String token = getAccessToken();
        String url = "https://api.spotify.com/v1/artists/"+artistID;
        String header = "Authorization:Bearer "+token;
        String data = makeGetReqeust(url, header);
        return data;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        APIPuller api = new APIPuller();
        String uzi_id = "4O15NlyKLIASxsJ0PrXPfz";
        String data = api.getArtist(uzi_id);
        System.out.println(data);
    }
}
