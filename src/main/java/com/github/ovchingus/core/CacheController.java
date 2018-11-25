package com.github.ovchingus.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Stream;

public class CacheController {

    private CacheController() {
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    // search oldest by creation time
    public static void deleteOld() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(Options.getCachePath()))) {
            Map<FileTime, Path> files = new TreeMap<>();
            for (Path file : stream) {
                if (!file.toFile().isDirectory()) {
                    files.put((Files.readAttributes(file, BasicFileAttributes.class).creationTime()), file);
                }
            }
            //sortByValue(files);
            Iterator<Map.Entry<FileTime, Path>> it = files.entrySet().iterator();
            int steps = files.size() - Options.getCachedFiles() + 1;
            for (int i = 0; i < steps; i++)
                if (it.hasNext()) {
                    Files.deleteIfExists(it.next().getValue());
                    it.remove();
                } else break;
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
    }

    public static void clean() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(Options.getCachePath()))) {
            Map<Integer, Path> files = new TreeMap<>();
            for (Path file : stream) {
                if (!file.toFile().isDirectory()) {
                    String fName = file.getFileName().toString();
                    files.put(Integer.parseInt(fName.substring(0, 3)), file);
                }
            }
            //sortByValue(files);
            Iterator<Map.Entry<Integer, Path>> it = files.entrySet().iterator();
            int steps = files.size() - Options.getCachedFiles();
            for (int i = 0; i < steps; i++)
                if (it.hasNext()) {
                    Files.deleteIfExists(it.next().getValue());
                    it.remove();
                } else break;
            System.out.println(files);
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
    }

    static String get(String url) {
        try {

            String a = String.valueOf(Files.readAllLines(Paths.get(Options.getCachePath() + fileNameFromUrl(url))));
            //because of returns string from file: [fileText] (deletes "[" and "]")
            return a.substring(1, a.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Cache error";
    }

    static void put(String url, String item) {
        deleteOld();
        Path path = Paths.get(Options.getCachePath() + fileNameFromUrl(url));
        try {
            Files.createFile(path);
            Files.write(path, item.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fileNameFromUrl(String url) {
        return url.substring(Options.getAPIRootURL().length() + 1)
                .replaceFirst("&api_key=" + Options.getAPIKey(), "");
    }

    static boolean cacheCheck(String url) {
        String fileName = fileNameFromUrl(url);

        File dir = new File(Options.getCachePath()); //path указывает на директорию
        List<File> lst = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));
        for (File item : lst)
            if (item.getName().equals(fileName))
                return true;
        return false;
    }


}
