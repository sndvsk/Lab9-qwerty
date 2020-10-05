package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class TeamTab implements Initializable {
    @FXML
    private Label team;
    @FXML
    private ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties properties = new Properties();
        Path propFile = Paths.get("");
        try{
            properties.load(Files.newBufferedReader(propFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}