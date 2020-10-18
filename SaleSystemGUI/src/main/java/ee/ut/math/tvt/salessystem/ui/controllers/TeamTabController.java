package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dto.Team;
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
        Team team = new Team();
        teamName.setText(team.getTeamName());
        teamLeader.setText(team.getTeamLeader());
        teamLeaderEmail.setText(team.getTeamLeaderEmail());
        teamMember1.setText(team.getTeamMember1());
        teamMember2.setText(team.getTeamMember2());
        teamMember3.setText(team.getTeamMember3());
        Properties properties = new Properties();
        Path propFile = Paths.get("../src/main/resources/application.properties");
        try{
            properties.load(Files.newBufferedReader(propFile));
            File file = new File(properties.getProperty("logoPath"));
            imageView.setImage(new Image(file.toURI().toString()));

        } catch (IOException e) {
            log.error(e.toString());
        }
    }

}