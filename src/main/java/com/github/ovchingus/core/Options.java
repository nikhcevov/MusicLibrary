package com.github.ovchingus.core;


public class Options {
    private static int searchLimit = 30;
    private static int foundResults;
    private static boolean autoCorrect = false;
    private static int cachedFiles = 50;
    private static String cachePath = "src/main/java/com/github/ovchingus/cache/";

    static String getAPIKey() {
        return "681a7e3b8f399718fdf98338420653d7";
    }

    static String getAPIRootURL() {
        return "http://ws.audioscrobbler.com/2.0/";
    }

    static int getSearchLimit() {
        return searchLimit;
    }

    public static void setSearchLimit(int searchLimit) {
        Options.searchLimit = searchLimit;
    }

    static int getFoundResults() {
        return foundResults;
    }

    static void setFoundResults(int foundResults) {
        Options.foundResults = foundResults;
    }

    static int isAutoCorrect() {
        if (autoCorrect)
            return 1;
        else return 0;
    }

    public static void setAutoCorrect(boolean autoCorrect) {
        Options.autoCorrect = autoCorrect;
    }

    static int getCachedFiles() {
        return cachedFiles;
    }

    public static void setCachedFiles(int cachedFiles) {
        Options.cachedFiles = cachedFiles;
    }

    static String getCachePath() {
        return cachePath;
    }

    static void setCachePath(String cachePath) {
        Options.cachePath = cachePath;
    }
}