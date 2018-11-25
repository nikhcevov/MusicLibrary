package com.github.ovchingus.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jayway.jsonpath.JsonPath;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.ovchingus.core.ConnectionController.callRequest;

public class Album {

    private Album() {
        defaultSettings();
    }

    private static void defaultSettings() {
    }

    ////////////////////
    // get ALBUM INFO // is ok
    ////////////////////
    private static Multimap<String, String> callAlbumInfo(String album, String artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=album.getInfo" +
                "&api_key=" +
                Options.getAPIKey() +
                "&autocorrect[" + Options.isAutoCorrect() + "]" +
                "&artist=" +
                artist.replaceAll(" ", "%20") +
                "&album=" +
                album.replaceAll(" ", "%20") +
                "&format=json";
        // Server part
        String response = callRequest(url);
        //Read JSON response and print
        try {
            Multimap<String, String> result = ArrayListMultimap.create();
            int tagsCount = JsonPath.read(response, "$.album.tags.tag.length()");
            int tracksCount = JsonPath.read(response, "$.album.tracks.track.length()");

            result.put("name", JsonPath.read(response, "$.album.name").toString());
            result.put("artist", JsonPath.read(response, "$.album.artist").toString());
            // TODO: add release date support
            //result.put("releasedate", JsonPath.read(response, "$.album.releasedate").toString());
            for (int i = 0; i < tagsCount; i++)
                result.put("tags", JsonPath.read(response, "$.album.tags.tag[" + i + "].name").toString());
            for (int i = 0; i < tracksCount; i++)
                result.put("tracks", JsonPath.read(response, "$.album.tracks.track[" + i + "].name").toString());

            //Output
            return result;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['album']"))
                System.out.println("album not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String getInfo(String album, String artist) {
        try {
            Multimap<String, String> result;
            result = callAlbumInfo(album, artist);
            if (result != null) {
                return "Name: " + result.get("name") + '\n' +
                        "artist: " + result.get("artist") + '\n' +
                        "Tags: " + result.get("tags") + '\n' +
                        "Tracks: " + result.get("tracks") + '\n';
            } else return ("album not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    ///////////////////
    // search ALBUM // is ok
    //////////////////
    private static List<Pair<String, String>> callSearch(String album) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=album.search" +
                "&api_key=" +
                Options.getAPIKey() +
                "&limit=" +
                Options.getSearchLimit() +
                "&album=" +
                album.replaceAll(" ", "%20") + "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            List<Pair<String, String>> result = new ArrayList<>();
            String foundTotal = JsonPath.read(response, "$.results.opensearch:totalResults");
            Options.setFoundResults(Integer.parseInt(foundTotal));
            int count = JsonPath.read(response, "$.results.albummatches.album.length()");

            for (int i = 0; i < count; i++) {
                result.add(new Pair<String, String>(JsonPath.read(response, "$.results.albummatches.album[" + i + "].artist").toString(),
                        JsonPath.read(response, "$.results.albummatches.album[" + i + "].name").toString()));
            }
            //Output
            return result;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['results']"))
                System.out.println("album not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String search(String album) {
        if (!album.isEmpty()) {
            try {
                List<Pair<String, String>> result = new ArrayList<>(Objects.requireNonNull(callSearch(album)));
                StringBuilder out = new StringBuilder();
                out.append("Total results: ").append(Options.getFoundResults()).append("\n")
                        .append("Shown: ").append(Options.getSearchLimit()).append('\n')
                        .append('\n');
                for (Pair item : result) {
                    out.append("Artist: ").append(item.getKey()).append('\n');
                    out.append("Album: ").append(item.getValue()).append('\n');
                    out.append('\n');
                }
                return out.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("Enter valid album name");
        return "error";
    }

}
