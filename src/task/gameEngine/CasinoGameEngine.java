package task.gameEngine;

import task.domain.ActionType;
import task.domain.Match;
import task.domain.Player;
import task.domain.PlayerAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CasinoGameEngine {

    private long casinoBalance = 0;
    private long tempCasinoBalance = 0;


    // pass an object reference, not an Object itself, so players will be changed
    public long Play(List<Match> matches, List<Player> players) {
        for (Player currentPlayer : players) {

            for (PlayerAction currentPlayerAction : currentPlayer.getActions()) {

                UUID matchId = currentPlayerAction.getMatchId();
                Match matchForPlayerAction = getMatchByID(matchId, matches).orElse(null);

                processAction(currentPlayer, currentPlayerAction, matchForPlayerAction);

                if (!currentPlayer.isLegitimate() && currentPlayer.getFirstIllegalAction() == null) {
                    currentPlayer.setFirstIllegalAction(currentPlayerAction);
                }
            }

            if (currentPlayer.isLegitimate()) {
                this.casinoBalance += this.tempCasinoBalance;
                currentPlayer.calculateWinRate();
            }
        }

        return casinoBalance;
    }

    private Optional<Match> getMatchByID(UUID matchId, List<Match> matches) {
        return matches.stream()
                .filter(match -> match.getMatchId().equals(matchId))
                .findFirst();
    }


    //PLAYER PROCESS ACTION
    public void processAction(Player player, PlayerAction action, Match match) {
        ActionType actionType = action.getActionType();
        switch (actionType) {
            case DEPOSIT:
                deposit(player, action.getCoins());
                break;
            case BET:
                bet(player, action.getCoins(), action.getSide(), match);
                break;
            case WITHDRAW:
                withdraw(player, action.getCoins());
                break;
            default:
                throw new RuntimeException("Unsupported action type: " + actionType);
        }
    }

    private void deposit(Player player, long amountOfCoins) {
        long newBalance = player.getBalance() + amountOfCoins;
        player.setBalance(newBalance);
    }

    private void bet(Player player, long bettingCoins, Character side, Match match) {
        player.incrementBetCount();

        if (player.getBalance() >= bettingCoins) {
            if (match == null) {
                throw new RuntimeException("Match has an empty value!");
            }

            char result = match.getResult();

            if ((side == 'A' && result == 'A') || (side == 'B' && result == 'B')) { //TODO: side == result????
                //Player wins the bet
                long payout = (long) Math.floor(bettingCoins * match.getRate(side));
                player.addToBalance(payout);
                player.incrementWinCount();

                this.tempCasinoBalance -= bettingCoins;

            } else if (result == 'D') {
                //Match is a draw, return the bet amount //TODO: 1. decide what to do with 'D' | 2. What should it do with balance?
            } else {
                //Player loses the bet
                player.subtractFromBalance(bettingCoins);

                this.tempCasinoBalance += bettingCoins;
            }

        } else {
            //Player perform illegal action: insufficient balance for the bet
            player.isNotLegitimate();
        }
    }

    private void withdraw(Player player, long amountOfCoins) {
        if (player.getBalance() >= amountOfCoins) {
            player.subtractFromBalance(amountOfCoins);
        } else {
            //Player perform illegal action: insufficient balance for withdrawal
            player.isNotLegitimate();
        }
    }

    public long getCasinoBalance() {
        return casinoBalance;
    }

}
