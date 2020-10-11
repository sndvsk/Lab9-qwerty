package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class TeamTabController implements Initializable {
    private static final Logger log = LogManager.getLogger(TeamTabController.class);

    @FXML
    private Label teamName;
    @FXML
    private Label teamLeader;
    @FXML
    private Label teamLeaderEmail;
    @FXML
    private Label teamMember1;
    @FXML
    private Label teamMember2;
    @FXML
    private Label teamMember3;
    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties properties = new Properties();
        Path propFile = Paths.get("../src/main/resources/application.properties");
        try{
            properties.load(Files.newBufferedReader(propFile));
            teamName.setText(properties.getProperty("teamName"));
            teamLeader.setText(properties.getProperty("teamLeader"));
            teamLeaderEmail.setText(properties.getProperty("teamLeaderEmail"));
            teamMember1.setText(properties.getProperty("teamMember1"));
            teamMember2.setText(properties.getProperty("teamMember2"));
            teamMember3.setText(properties.getProperty("teamMember3"));
            File file = new File(properties.getProperty("logoPath"));
            imageView.setImage(new Image(file.toURI().toString()));

        } catch (IOException e) {
            log.error(e.toString());
        }
    }

}