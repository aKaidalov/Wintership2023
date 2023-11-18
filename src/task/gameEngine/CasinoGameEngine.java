package task.gameEngine;

import task.domain.ActionType;
import task.domain.Match;
import task.domain.Player;
import task.domain.PlayerAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CasinoGameEngine {
    // pass an object reference, not an Object itself, so players will be changed
    public void Play(List<Match> matches, List<Player> players) {
        for (Player currentPlayer : players) {
            for (PlayerAction currentPlayerAction : currentPlayer.getActions()) {

                UUID matchId = currentPlayerAction.getMatchId();
                Match matchForPlayerAction = getMatchByID(matchId, matches).get();

                currentPlayer.processAction(currentPlayerAction, matchForPlayerAction);
            }
        }
    }

    private Optional<Match> getMatchByID(UUID matchId, List<Match> matches) {
        return matches.stream()
                .filter(match -> match.getMatchId().equals(matchId))
                .findFirst();
    }

}
