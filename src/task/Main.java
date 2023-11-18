package task;

import task.domain.Match;
import task.domain.Player;
import task.domain.PlayerAction;
import task.helpers.Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String MATCH_DATA_FILE_PATH = "src" + File.separator + "task" + File.separator + "data" + File.separator + "match_data.txt";
    private static final String PLAYER_DATA_FILE_PATH = "src" + File.separator + "task" + File.separator + "data" + File.separator + "player_data.txt";

    public static void main(String[] args) {
        //read data from "player_data.txt" and "match_data.txt"
        List<Match> matches = Helpers.readFromMatchData(MATCH_DATA_FILE_PATH);
        Helpers.printCollectedDataFromFiles(matches);

        System.out.println("\n");

        List<Player> players = Helpers.readFromPlayerData(PLAYER_DATA_FILE_PATH);
        Helpers.printCollectedDataFromFiles(players);


        //sort players for legitimatePlayers and illegitimatePlayers


        //*need to sort players by id
        //write results into result.txt - legalList; illegalList; casinoAccount
    }
}