package com.github.ovchingus.controller;

import com.github.ovchingus.core.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class AppController {

    @FXML
    private TextArea textAreaArtist;
    /**
     * Artist scene
     */
    @FXML
    private Button searchButtonArtist;
    private ObservableList<String> methodChooseListArtist = FXCollections.observableArrayList("Info", "Tracks", "Albums", "Search");
    @FXML
    private ChoiceBox<String> methodChooseBoxArtist;
    @FXML
    private TextField artistNameTextFieldArtist;

    /**
     * Album scene
     */
    @FXML
    private ChoiceBox<String> methodChooseBoxAlbum;
    @FXML
    private TextArea textAreaAlbum;
    @FXML
    private TextField artistNameTextFieldAlbum;
    @FXML
    private TextField albumNameTextFieldAlbum;
    @FXML
    private Button searchButtonAlbum;
    private ObservableList<String> methodChooseListAlbum = FXCollections.observableArrayList("Info", "Search");

    /**
     * Track scene
     */
    @FXML
    private ChoiceBox<String> methodChooseBoxTrack;
    @FXML
    private TextField artistNameTextFieldTrack;
    @FXML
    private TextField trackNameTextFieldTrack;
    @FXML
    private Button searchButtonTrack;
    private ObservableList<String> methodChooseListTrack = FXCollections.observableArrayList("Info", "Search");
    @FXML
    private TextArea textAreaTrack;


    /**
     * Tag scene
     */
    @FXML
    private ChoiceBox<String> methodChooseBoxTag;
    @FXML
    private TextField tagNameTextFieldTag;
    @FXML
    private Button searchButtonTag;
    @FXML
    private TextArea textAreaTags;
    private ObservableList<String> methodChooseListTag = FXCollections.observableArrayList("Info", "Tracks", "Albums", "Artists");

    /**
     * Options scene
     */
    @FXML
    private CheckBox autoCorrectButtonOptions;
    @FXML
    private TextField maxSearchResultsOptions;
    @FXML
    private Button applyButtonOptions;
    @FXML
    private TextField maxCachedFilesOptions;

    public AppController() {

    }

    public void initialize() {
        // Artist
        methodChooseBoxArtist.setValue("Info");
        methodChooseBoxArtist.setItems(methodChooseListArtist);
        searchButtonArtist.setOnAction(e -> getSearchArtist(methodChooseBoxArtist));

        // Album
        methodChooseBoxAlbum.setOnAction(e -> getChooseBoxActionAlbum(methodChooseBoxAlbum));
        methodChooseBoxAlbum.setValue("Info");
        methodChooseBoxAlbum.setItems(methodChooseListAlbum);
        searchButtonAlbum.setOnAction(e -> getSearchAlbum(methodChooseBoxAlbum));

        // Track
        methodChooseBoxTrack.setOnAction(e -> getChooseBoxActionTrack(methodChooseBoxTrack));
        methodChooseBoxTrack.setValue("Info");
        methodChooseBoxTrack.setItems(methodChooseListTrack);
        searchButtonTrack.setOnAction(e -> getSearchTrack(methodChooseBoxTrack));

        // Tag
        methodChooseBoxTag.setValue("Info");
        methodChooseBoxTag.setItems(methodChooseListTag);
        searchButtonTag.setOnAction(e -> getSearchTag(methodChooseBoxTag));

        // Options
        applyButtonOptions.setOnAction(e -> applyOptions());

    }

    private void applyOptions() {
        if (!maxSearchResultsOptions.getText().isEmpty()) {
            int searchLimit = Integer.parseInt(maxSearchResultsOptions.getText());
            Options.setSearchLimit(searchLimit);
        }

        if (!maxCachedFilesOptions.getText().isEmpty()) {
            int cachedFiles = Integer.parseInt(maxCachedFilesOptions.getText());
            Options.setCachedFiles(cachedFiles);
        }
        if (autoCorrectButtonOptions.isSelected())
            Options.setAutoCorrect(true);
        else Options.setAutoCorrect(false);

    }

    private void getSearchArtist(ChoiceBox<String> artistChooseBox) {
        String method = artistChooseBox.getValue();
        if (method.equals("Info"))
            textAreaArtist.setText(Artist.getInfo(artistNameTextFieldArtist.getText()));
        if (method.equals("Tracks"))
            textAreaArtist.setText(Artist.getTopTracks(artistNameTextFieldArtist.getText()));
        if (method.equals("Albums"))
            textAreaArtist.setText(Artist.getTopAlbums(artistNameTextFieldArtist.getText()));
        if (method.equals("Search"))
            textAreaArtist.setText(Artist.search(artistNameTextFieldArtist.getText()));
    }

    private void getSearchAlbum(ChoiceBox<String> albumChooseBox) {
        String method = albumChooseBox.getValue();
        if (method.equals("Info")) {
            textAreaAlbum.setText(Album.getInfo(albumNameTextFieldAlbum.getText(), artistNameTextFieldAlbum.getText()));
        }
        if (method.equals("Search")) {
            textAreaAlbum.setText(Album.search(albumNameTextFieldAlbum.getText()));
        }
    }

    private void getChooseBoxActionAlbum(ChoiceBox<String> albumChooseBox) {
        String method = albumChooseBox.getValue();
        if (method.equals("Info"))
            artistNameTextFieldAlbum.setDisable(false);
        if (method.equals("Search"))
            artistNameTextFieldAlbum.setDisable(true);
    }

    private void getSearchTrack(ChoiceBox<String> trackChooseBox) {
        String method = trackChooseBox.getValue();
        if (method.equals("Info"))
            textAreaTrack.setText(Track.getInfo(trackNameTextFieldTrack.getText(), artistNameTextFieldTrack.getText()));
        if (method.equals("Search"))
            textAreaTrack.setText(Track.multiSearch(trackNameTextFieldTrack.getText(), artistNameTextFieldTrack.getText()));
    }

    private void getChooseBoxActionTrack(ChoiceBox<String> trackChooseBox) {
        String method = trackChooseBox.getValue();
        if (method.equals("Search"))
            artistNameTextFieldTrack.setPromptText("Optional");
    }

    private void getSearchTag(ChoiceBox<String> tagChooseBox) {
        String method = tagChooseBox.getValue();
        if (method.equals("Info"))
            textAreaTags.setText(Tag.getInfo(tagNameTextFieldTag.getText()));
        if (method.equals("Tracks"))
            textAreaTags.setText(Tag.getTopTracks(tagNameTextFieldTag.getText()));
        if (method.equals("Albums"))
            textAreaTags.setText(Tag.getTopAlbums(tagNameTextFieldTag.getText()));
        if (method.equals("Artists"))
            textAreaTags.setText(Tag.getTopArtists(tagNameTextFieldTag.getText()));
    }

}
