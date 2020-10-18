package ee.ut.math.tvt.salessystem.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Team {

    private final String teamName;
    private final String teamLeader;
    private final String teamLeaderEmail;
    private final String teamMember1;
    private final String teamMember2;
    private final String teamMember3;



    public Team() throws IOException {
        Properties properties = new Properties();
        Path propFile = Paths.get("../src/main/resources/application.properties");
        properties.load(Files.newBufferedReader(propFile));
        teamName = (properties.getProperty("teamName"));
        teamLeader = (properties.getProperty("teamLeader"));
        teamLeaderEmail = (properties.getProperty("teamLeaderEmail"));
        teamMember1 = (properties.getProperty("teamMember1"));
        teamMember2 = (properties.getProperty("teamMember2"));
        teamMember3 = (properties.getProperty("teamMember3"));

    }

    @Override
    public String toString() {
        return "Team: \n" +
                "teamName='" + teamName + "',\n" +
                " teamLeader='" + teamLeader + "',\n" +
                " teamLeaderEmail='" + teamLeaderEmail + "',\n" +
                " teamMember1='" + teamMember1 + "',\n" +
                " teamMember2='" + teamMember2 + "',\n" +
                " teamMember3='" + teamMember3 + "'.\n";
    }
}