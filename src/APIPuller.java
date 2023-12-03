
import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

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

    public Map<String, ArrayList<Map<String, Object>>>  querySpotify(String track) throws IOException, InterruptedException {
        String query = "https://api.spotify.com/v1/search?q=";
        query += track + "&type=track";
        String token = getAccessToken();
        query = query.replace(" ", "%20");
        String url = query;
        String header = "Authorization:Bearer "+token;
        String data = makeGetReqeust(url, header);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        Map<String, ArrayList<Map<String, Object>>> querySet = new LinkedHashMap<String, ArrayList<Map<String, Object>>>();
        ArrayList<Map<String, Object>> trackSet = this.parseQuerySet(map, "tracks");
        querySet.put("tracks", trackSet);
//        if (has_artist) {
//            ArrayList<Map<String, Object>> artistSet = this.parseQuerySet(map, "artists");
//            ArrayList<Map<String, Object>> parsedArtistSet = new ArrayList<Map<String, Object>>();
//            for (Map<String, Object> artistMap : artistSet) {
//                String artistID = (String) artistMap.get("id");
//                Map<String, Object> fullArtist = this.getArtist(artistID);
//                parsedArtistSet.add(this.parseArtist((LinkedHashMap<String, Object>) fullArtist));
//            }
//            querySet.put("artists", parsedArtistSet);
//        } else {
//            querySet.put("artists", new ArrayList<Map<String, Object>>());
//        }
//        if (has_album) {
//            ArrayList<Map<String, Object>> albumSet = this.parseQuerySet(map, "albums");
//            querySet.put("albums", albumSet);
//        } else {
//            querySet.put("albums", new ArrayList<Map<String, Object>>());
//        }
//        if (has_track) {
//
//        } else {
//            querySet.put("tracks", new ArrayList<Map<String, Object>>());
//        }
        return querySet;
    }

    public Map<String, Object> getArtist(String artistID) throws IOException, InterruptedException {
        String token = getAccessToken();
        String url = "https://api.spotify.com/v1/artists/"+artistID;
        String header = "Authorization:Bearer "+token;
        String data = makeGetReqeust(url, header);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        Map<String, Object> artist = this.parseArtist((LinkedHashMap<String, Object>) map);
        return artist;
    }

    public Map<String, Object> getAlbum(String albumID) throws IOException, InterruptedException {
        String token = getAccessToken();
        String url = "https://api.spotify.com/v1/albums/"+albumID;
        String header = "Authorization:Bearer "+token;
        String data = makeGetReqeust(url, header);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        Map<String, Object> album = this.parseAlbum((LinkedHashMap<String, Object>) map);
        return album;
    }

    public Map<String, Object> getTrack(String trackID) throws IOException, InterruptedException {
        String token = getAccessToken();
        String url = "https://api.spotify.com/v1/tracks/"+trackID;
        String header = "Authorization:Bearer "+token;
        String data = makeGetReqeust(url, header);
        Map<String, Object> returnTrackData = new LinkedHashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        // Load the album
        LinkedHashMap<String, Object> albumData = (LinkedHashMap<String, Object>) map.get("album");
        Map<String, Object> album = this.parseAlbum(albumData);
        String albumID = (String) album.get("id");
        Map<String, Object> fullAlbum = this.getAlbum(albumID);;
        returnTrackData.put("album", fullAlbum);
        // Load the artist
        ArrayList<LinkedHashMap<String, Object>> items = (ArrayList<LinkedHashMap<String, Object>>) map.get("artists");
        ArrayList<Map<String, Object>> itemSet = new ArrayList<Map<String, Object>>();
        for (LinkedHashMap<String, Object> item : items) {
            Map<String, Object> fullArtist = this.getArtist((String) item.get("id"));
            itemSet.add(fullArtist);
        }
        returnTrackData.put("artists", itemSet);
        // Load the track
        returnTrackData.put("track", this.parseTrack((LinkedHashMap<String, Object>) map));
        return returnTrackData;
    }

    public Map<String, Object> parseArtist(LinkedHashMap<String, Object> artist) {
        Map<String, Object> artistMap = new LinkedHashMap<String, Object>();
        artistMap.put("name", artist.get("name"));
        artistMap.put("id", artist.get("id"));
        artistMap.put("popularity", artist.get("popularity"));
        artistMap.put("uri", artist.get("uri"));
        ArrayList<String> genres = (ArrayList<String>) artist.get("genres");
        artistMap.put("genres", genres);
        LinkedHashMap<String, Object> followers = (LinkedHashMap<String, Object>) artist.get("followers");
        if (followers == null) {
            artistMap.put("followers", 0);
            return artistMap;
        }
        artistMap.put("followers", followers.get("total"));
        return artistMap;
    }

    public Map<String, Object> parseAlbum(LinkedHashMap<String, Object> album) {
        Map<String, Object> albumMap = new LinkedHashMap<String, Object>();
        albumMap.put("name", album.get("name"));
        albumMap.put("id", album.get("id"));
        albumMap.put("type", album.get("type"));
        albumMap.put("total_tracks", album.get("total_tracks"));
        albumMap.put("uri", album.get("uri"));
        albumMap.put("release_date", album.get("release_date"));
        return albumMap;
    }

    public Map<String, Object> parseTrack(LinkedHashMap<String, Object> track) {
        Map<String, Object> trackMap = new LinkedHashMap<String, Object>();
        trackMap.put("name", track.get("name"));
        trackMap.put("id", track.get("id"));
        trackMap.put("type", track.get("type"));
        trackMap.put("duration_ms", track.get("duration_ms"));
        trackMap.put("explicit", track.get("explicit"));
        trackMap.put("uri", track.get("uri"));
        ArrayList<String> artistIDs = new ArrayList<String>();

        return trackMap;
    }
    public ArrayList<Map<String, Object>> parseQuerySet(Map<String, Object> map, String type) {
        Map<String, Object> artists = (Map<String, Object>) map.get(type);
        ArrayList<LinkedHashMap<String, Object>> items = (ArrayList<LinkedHashMap<String, Object>>) artists.get("items");
        ArrayList<Map<String, Object>> itemSet = new ArrayList<Map<String, Object>>();
        for (LinkedHashMap<String, Object> item : items) {
            if (type.equals("artists")) {
                itemSet.add(this.parseArtist(item));
            } else if (type.equals("albums")) {
                itemSet.add(this.parseAlbum(item));
            } else if (type.equals("tracks")) {
                itemSet.add(this.parseTrack(item));
            }
        }
        return itemSet;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        APIPuller api = new APIPuller();
        String trackID = "4FyesJzVpA39hbYvcseO2d";
        try {
            Map<String, Object> track = api.getTrack(trackID);
            System.out.println(track);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String artistID = "4O15NlyKLIASxsJ0PrXPfz";
//        String artist = "";
//        String album = "";
//        String track = "just wanna rock";
//        try {
//            Map<String, ArrayList<Map<String, Object>>>  querySet = api.querySpotify(artist, album, track);
//            System.out.println(querySet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
