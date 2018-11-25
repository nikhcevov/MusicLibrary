package com.github.ovchingus.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jayway.jsonpath.JsonPath;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.ovchingus.core.ConnectionController.callRequest;

public class Track {

    private Track() {
        defaultSettings();
    }

    private static void defaultSettings() {
    }

    ////////////////
    // Info TRACK //
    ////////////////
    private static Multimap<String, String> callTrackInfo(String track, String artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=track.getInfo" +
                "&api_key=" +
                Options.getAPIKey() +
                "&autocorrect[" + Options.isAutoCorrect() + "]" +
                "&artist=" +
                artist.replaceAll(" ", "%20") +
                "&track=" +
                track.replaceAll(" ", "%20") +
                "&format=json";
        // Server part
        String response = callRequest(url);
        //Read JSON response and print
        try {
            Multimap<String, String> result = ArrayListMultimap.create();
            int tagsCount = JsonPath.read(response, "$.track.toptags.tag.length()");

            result.put("name", JsonPath.read(response, "$.track.name").toString());
            result.put("artist", JsonPath.read(response, "$.track.artist.name").toString());
            result.put("album", JsonPath.read(response, "$.track.album.title"));
            int dur = Integer.parseInt(JsonPath.read(response, "$.track.duration").toString()) / 60000;
            result.put("duration", Integer.toString(dur) + ".00");
            for (int i = 0; i < tagsCount; i++)
                result.put("tags", JsonPath.read(response, "$.track.toptags.tag[" + i + "].name"));

            String desc = JsonPath.read(response, "$.track.wiki.summary");
            result.put("wiki", desc.substring(+0, desc.indexOf(" <a ") - 1));

            //Output
            return result;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['track']"))
                System.out.println("track not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String getInfo(String track, String artist) {
        try {
            Multimap<String, String> result;
            result = callTrackInfo(track, artist);
            if (result != null) {
                return "Track: " + result.get("name") + '\n' +
                        "Artist: " + result.get("artist") + '\n' +
                        "Album: " + result.get("album") + '\n' +
                        "Duration: " + result.get("duration") + '\n' +
                        "Tags: " + result.get("tags") + '\n' +
                        "Description: " + result.get("wiki") + '\n';
            } else return ("track not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    //////////////////
    // Search TRACK //
    //////////////////
    private static List<Pair<String, String>> callSearch(String track, String... artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=track.search" +
                "&api_key=" +
                Options.getAPIKey() +
                "&track=" +
                track.replaceAll(" ", "%20") +
                "&limit=" +
                Options.getSearchLimit();
        if (!artist[0].isEmpty())
            url += "&artist=" + artist[0].replaceAll(" ", "%20") + "&format=json";
        else url += "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            List<Pair<String, String>> pairList = new ArrayList<>();
            String foundTotal = JsonPath.read(response, "$.results.opensearch:totalResults");
            Options.setFoundResults(Integer.parseInt(foundTotal));
            int count = JsonPath.read(response, "$.results.trackmatches.track.length()");
            for (int i = 0; i < count; i++) {

                pairList.add(new Pair<>(JsonPath.read(response, "$.results.trackmatches.track[" + i + "].artist"),
                        JsonPath.read(response, "$.results.trackmatches.track[" + i + "].name")));
            }
            //Output
            //System.out.println("Total: " + total);
            return pairList;

        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['results']"))
                System.out.println("track not found.");
            return null;
            //throw new RuntimeException(e.getMessage());
        }
    }

    private static String searchTemplate(String track, String isArtist) {
        try {
            List<Pair<String, String>> result = new ArrayList<>(Objects.requireNonNull(callSearch(track, isArtist)));

            StringBuilder out = new StringBuilder();
            if (!result.isEmpty()) {
                out.append("Total results: ").append(Options.getFoundResults()).append("\n")
                        .append("Shown: ").append(Options.getSearchLimit()).append('\n');
                for (Pair<String, String> aResult : result)
                    out.append("Artist: ").append(aResult.getKey()).append("\n")
                            .append("track: ").append(aResult.getValue()).append("\n");
                return out.toString();
            }
            return ("track not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static String search(String track) {
        if (!track.isEmpty())
            return searchTemplate(track, "");
        else return ("Enter valid track name");
    }

    public static String search(String track, String artist) {
        if (!track.isEmpty() && !artist.isEmpty())
            return searchTemplate(track, artist);
        else return ("Enter valid information");
    }

    // leave "" for artist to search only by track name
    public static String multiSearch(String track, String artist) {
        if (!track.isEmpty())
            return searchTemplate(track, artist);
        else return ("Enter valid information");
    }
}
