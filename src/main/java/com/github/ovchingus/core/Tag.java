package com.github.ovchingus.core;

import com.jayway.jsonpath.JsonPath;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.ovchingus.core.ConnectionController.callRequest;

public class Tag {

    private Tag() {
        defaultSettings();
    }

    private static void defaultSettings() {
    }


    //////////////
    // get INFO // is ok
    /////////////
    private static String callGetInfo(String tag) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=tag.getInfo" +
                "&api_key=" +
                Options.getAPIKey() +
                "&tag=" +
                tag.replaceAll(" ", "%20") + "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            //Output
            return JsonPath.read(response, "$.tag.wiki.summary");
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['tag']"))
                System.out.println("tag not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String getInfo(String tag) {
        if (!tag.isEmpty()) {
            try {
                String result = Objects.requireNonNull(callGetInfo(tag));
                String info = result.substring(+0, result.lastIndexOf(" <a "));
                if (!info.isEmpty())
                    return ("Information: " + result.substring(+0, result.lastIndexOf(" <a ")));
                else return ("There is no info");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("Enter valid tag");
        return "error";
    }


    ////////////////
    // get ALBUMS // is ok
    ///////////////
    private static List<Pair<String, String>> callGetTopAlbums(String tag) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=tag.getTopAlbums" +
                "&api_key=" +
                Options.getAPIKey() +
                "&limit=" +
                Options.getSearchLimit() +
                "&tag=" +
                tag.replaceAll(" ", "%20") + "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            int count = JsonPath.read(response, "$.albums.album.length()");
            List<Pair<String, String>> result = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                result.add(new Pair<>(JsonPath.read(response, "$.albums.album[" + i + "].artist.name").toString(),
                        JsonPath.read(response, "$.albums.album[" + i + "].name").toString()));
            }
            //Output
            return result;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['topalbums']"))
                System.out.println("Albums not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String getTopAlbums(String tag) {
        if (!tag.isEmpty()) {
            try {
                List<Pair<String, String>> result = new ArrayList<>(Objects.requireNonNull(callGetTopAlbums(tag)));
                StringBuilder out = new StringBuilder();
                for (Pair item : result) {
                    out.append("Artist: ").append(item.getKey()).append('\n');
                    out.append("Album: ").append(item.getValue()).append('\n')
                            .append('\n');
                }
                return out.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("Enter valid tag");
        return "error";
    }


    /////////////////
    // get ARTISTS // is ok
    ////////////////
    private static List<String> callGetTopArtists(String tag) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=tag.getTopArtists" +
                "&api_key=" +
                Options.getAPIKey() +
                "&limit=" +
                Options.getSearchLimit() +
                "&tag=" +
                tag.replaceAll(" ", "%20") + "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            //Output
            int count = JsonPath.read(response, "$.topartists.artist.length()");
            List<String> result = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                result.add(JsonPath.read(response, "$.topartists.artist[" + i + "].name"));
            }
            return result;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['topartists']"))
                System.out.println("Artists not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String getTopArtists(String tag) {
        if (!tag.isEmpty()) {
            try {
                List<String> result = new ArrayList<>(Objects.requireNonNull(callGetTopArtists(tag)));
                StringBuilder out = new StringBuilder();
                for (String item : result)
                    out.append("Artist: ").append(item).append('\n');
                return out.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("Enter valid tag");
        return "error";
    }


    ////////////////
    // get TRACKS // is ok
    ///////////////
    private static List<Pair<String, String>> callGetTopTracks(String tag) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=tag.getTopTracks" +
                "&api_key=" +
                Options.getAPIKey() +
                "&limit=" +
                Options.getSearchLimit() +
                "&tag=" +
                tag.replaceAll(" ", "%20") + "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            int count = JsonPath.read(response, "$.tracks.track.length()");
            List<Pair<String, String>> result = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                result.add(new Pair<>(JsonPath.read(response, "$.tracks.track[" + i + "].artist.name").toString(),
                        JsonPath.read(response, "$.tracks.track[" + i + "].name").toString()));
            }
            //Output
            return result;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['tracks']"))
                System.out.println("Tracks not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String getTopTracks(String tag) {
        if (!tag.isEmpty()) {
            try {
                List<Pair<String, String>> result = new ArrayList<>(Objects.requireNonNull(callGetTopTracks(tag)));
                StringBuilder out = new StringBuilder();
                for (Pair item : result) {
                    out.append("Artist: ").append(item.getKey()).append('\n');
                    out.append("Track: ").append(item.getValue()).append('\n')
                            .append('\n');
                }
                return out.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("Enter valid tag");
        return "error";
    }

}
