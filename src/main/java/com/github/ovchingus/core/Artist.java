package com.github.ovchingus.core;

import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.github.ovchingus.core.ConnectionController.callRequest;

public class Artist {

    private Artist() {
        defaultSettings();
    }

    private static void defaultSettings() {
    }

    ///////////////////
    // Search ARTIST //
    ///////////////////
    private static List<String> callSearch(String artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=artist.search" +
                "&api_key=" +
                Options.getAPIKey() +
                "&limit=" +
                Options.getSearchLimit() +
                "&artist=" +
                artist.replaceAll(" ", "%20") + "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            List<String> info = new ArrayList<>();
            String foundTotal = JsonPath.read(response, "$.results.opensearch:totalResults");
            Options.setFoundResults(Integer.parseInt(foundTotal));
            int count = JsonPath.read(response, "$.results.artistmatches.artist.length()");

            for (int i = 0; i < count; i++)
                info.add(JsonPath.read(response, "$.results.artistmatches.artist[" + i + "].name").toString());

            //Output
            return info;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['results']"))
                System.out.println("Track not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    public static String search(String artist) {
        if (!artist.isEmpty()) {
            try {
                List<String> result = new ArrayList<>(Objects.requireNonNull(callSearch(artist)));
                StringBuilder out = new StringBuilder();
                out.append("Total results: ").append(Options.getFoundResults()).append("\n")
                        .append("Shown: ").append(Options.getSearchLimit()).append('\n')
                        .append('\n');
                for (String item : result) {
                    out.append("Artist: ").append(item).append('\n');
                }

                return out.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else return ("Enter valid artist name");
        return "error";
    }


    //////////////////////////////
    // Search ARTIST TRACKS //
    /////////////////////////////
    private static List<String> callGetTopTracks(String artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=artist.gettoptracks" +
                "&api_key=" +
                Options.getAPIKey() +
                "&artist=" +
                artist.replaceAll(" ", "%20") + "&autocorrect[" + Options.isAutoCorrect() + "]" +
                "&limit=" +
                Options.getSearchLimit() +
                "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            List<String> result = new ArrayList<>();
            int count = JsonPath.read(response, "$.toptracks.track.length()");
            for (int i = 0; i < count; i++) {
                result.add(JsonPath.read(response, "$.toptracks.track.[" + i + "].name"));
            }
            return result;

        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['toptracks']"))
                System.out.println("Track not found.");
            return null;
        }
    }

    public static String getTopTracks(String artist) {
        try {
            List<String> result = new ArrayList<String>(Objects.requireNonNull(callGetTopTracks(artist)));
            StringBuilder out = new StringBuilder();

            if (!result.isEmpty()) {
                out.append("Shown: ").append(Options.getSearchLimit()).append('\n')
                        .append('\n');
                for (String item : result)
                    out.append("Track: ").append(item).append('\n');
                return out.toString();
            } else return "Track not found";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    /////////////////////
    // Get ARTIST INFO //
    /////////////////////
    private static List<String> callGetInfo(String artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=artist.getInfo" +
                "&api_key=" +
                Options.getAPIKey() +
                "&autocorrect[" + Options.isAutoCorrect() + "]" +
                "&artist=" +
                artist.replaceAll(" ", "%20") +
                "&format=json";
        // Server part
        String response = callRequest(url);
        //Read JSON response and print
        try {
            List<String> info = new ArrayList<>(3);
            int tagsCount = JsonPath.read(response, "$.artist.tags.tag.length()");
            info.add(JsonPath.read(response, "$.artist.name").toString());
            info.add(JsonPath.read(response, "$.artist.bio.summary"));
            for (int i = 0; i < tagsCount; i++) {
                info.add(JsonPath.read(response, "$.artist.tags.tag[" + i + "].name").toString());
            }
            //Output
            return info;
        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['artist']"))
                System.out.println("Artist not found.");
            //throw new RuntimeException(e.getMessage());
            return null;
        }
    }


    //TODO: сделано сверхчеловеком
    public static String getInfo(String artist) {
        try {
            List<String> result;
            result = callGetInfo(artist);
            if (result != null) {
                Iterator<String> it = result.iterator();
                StringBuilder out = new StringBuilder();
                out.append("Artist: ").append(result.get(0)).append('\n');
                out.append("Tags: ");
                it.next();
                it.next();
                while (it.hasNext()) {
                    out.append(it.next()).append(", ");
                }
                out.delete(out.length() - 2, out.length()).append('\n');
                out.append("Biography: ");
                out.append(result.get(1));
                out.delete(out.lastIndexOf(" <a"), out.length());
                return out.toString();
            } else return ("Artist not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    ////////////////////////
    // Get ARTIST ALBUMS // is ok
    ///////////////////////
    private static List<String> callGetTopAlbums(String artist) throws Exception {
        String url = Options.getAPIRootURL() +
                "?method=artist.gettopalbums" +
                "&api_key=" +
                Options.getAPIKey() +
                "&artist=" +
                artist.replaceAll(" ", "%20") + "&autocorrect[" + Options.isAutoCorrect() + "]" +
                "&limit=" +
                Options.getSearchLimit() +
                "&format=json";

        // Server part
        String response = callRequest(url);

        //Read JSON response and print
        try {
            List<String> result = new ArrayList<>();
            int count = JsonPath.read(response, "$.topalbums.album.length()");
            for (int i = 0; i < count; i++) {
                result.add(JsonPath.read(response, "$.topalbums.album.[" + i + "].name"));
            }
            return result;

        } catch (Exception e) {
            if (e.getMessage().equals("Missing property in path $['topalbums']"))
                System.out.println("Album not found.");
            return null;
        }
    }

    public static String getTopAlbums(String artist) {
        try {
            List<String> result = new ArrayList<String>(Objects.requireNonNull(callGetTopAlbums(artist)));
            StringBuilder out = new StringBuilder();

            if (!result.isEmpty()) {
                out.append("Shown: ").append(Options.getSearchLimit()).append('\n')
                        .append('\n');
                for (String item : result)
                    out.append("Album: ").append(item).append('\n');
                return out.toString();
            } else return ("Album not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

}
