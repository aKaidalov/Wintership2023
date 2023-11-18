package task.helpers;

import task.domain.ActionType;
import task.domain.Match;
import task.domain.Player;
import task.domain.PlayerAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Helpers {
    //read and WRITE from file

    // MATCH
    public static List<Match> readFromMatchData(String matchDatafilePath) {
        List<Match> matches = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(matchDatafilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Match newMatch = processMatchData(line);
                matches.add(newMatch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }

    private static Match processMatchData(String line) {
        //Split the line into components
        String[] parts = line.split(",");

        //Create Match object
        UUID matchId = UUID.fromString(parts[0]);
        double rateA = Double.parseDouble(parts[1]);
        double rateB = Double.parseDouble(parts[2]);
        char result = parts[3].charAt(0);

        return new Match(matchId, rateA, rateB, result);
    }


    // PLAYER
    public static List<Player> readFromPlayerData(String playerDatafilePath) { //return an array of unique players
        Map<UUID, Player> uniquePlayers = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(playerDatafilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Player newPlayer = processPlayerData(line);
                UUID newPlayerId = newPlayer.getPlayerId();
                PlayerAction newPlayerAction = newPlayer.getActions().get(0);

                //if player already exists - add PlayerAction
                if (uniquePlayers.containsKey(newPlayerId)){
                    Player playerToModify = uniquePlayers.get(newPlayerId);
                    playerToModify.addPlayerAction(newPlayerAction);
                } else {
                    uniquePlayers.put(newPlayerId, newPlayer);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(uniquePlayers.values());
    }

    private static Player processPlayerData(String line) {
        //Split the line into components
        String[] parts = line.split(",");

        //Create a new Player
        UUID playerId = UUID.fromString(parts[0]);

        Player newPlayer = new Player(playerId);

        //Create PlayerAction
        ActionType actionType = ActionType.valueOf(parts[1]);
        UUID matchId = parts[2].isEmpty() ? null : UUID.fromString(parts[2]);
        int coins = parts[3].isEmpty() ? 0 : Integer.parseInt(parts[3]);
        Character side = isSide(parts) ? parts[4].charAt(0) : null;

        PlayerAction action = new PlayerAction(actionType, matchId, coins, side);

        //Add Action to PLayer's actions
        newPlayer.addPlayerAction(action);

        return newPlayer;
    }

    private static boolean isSide(String[] parts) {
        if (parts.length > 4 && !parts[4].isEmpty())
            if ("A".equals(parts[4]) || "B".equals(parts[4]))
                return true;

        return false;
    }


    //print data from files
    public static <T> void printCollectedDataFromFiles(List<T> list) {
        int matchCount = 0;
        int playerCount = 0;

        for (T element:list) {
            if (element instanceof Player) {
                playerCount++;
                System.out.println("\n" + playerCount + ". " + element);

                int actionCount = 0;
                for (PlayerAction action : ((Player) element).getActions()) {
                    actionCount++;
                    System.out.println("      " + actionCount + ". " + action);
                }
            } else {
                matchCount++;
                System.out.println(matchCount + ". " + element);
            }
        }
    }
}
