package task.gameEngine;

import task.domain.ActionType;
import task.domain.Match;
import task.domain.Player;
import task.domain.PlayerAction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CasinoGameEngine {

    private long casinoBalance = 0;
    private long tempCasinoBalance = 0;


    //After all matches were played return final casinoBalance
    public long Play(List<Match> matches, List<Player> players) {

        for (Player currentPlayer : players) {
            processPlayerActions(matches, currentPlayer);
        }

        return casinoBalance;
    }

    private void processPlayerActions(List<Match> matches, Player currentPlayer) {
        for (PlayerAction currentPlayerAction : currentPlayer.getActions()) {

            UUID matchId = currentPlayerAction.getMatchId();
            Match matchForPlayerAction = getMatchByID(matchId, matches).orElse(null);

            processAction(currentPlayer, currentPlayerAction, matchForPlayerAction);

            if (!currentPlayer.isLegitimate() && currentPlayer.getFirstIllegalAction() == null) {
                currentPlayer.setFirstIllegalAction(currentPlayerAction);
            }
        }

        if (currentPlayer.isLegitimate()) {
            updateCasinoBalance();
            currentPlayer.calculateWinRate();
        }

        resetTempCasinoBalance(); //Reset tempCasinoBalance after updating casinoBalance
    }

    private Optional<Match> getMatchByID(UUID matchId, List<Match> matches) {
        return matches.stream()
                .filter(match -> match.getMatchId().equals(matchId))
                .findFirst();
    }

    private void updateCasinoBalance() {
        this.casinoBalance += this.tempCasinoBalance;
    }
    private void resetTempCasinoBalance() {
        this.tempCasinoBalance = 0;
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
        long updatedBalance = player.getBalance() + amountOfCoins;
        player.setBalance(updatedBalance);
    }

    private void bet(Player player, long bettingCoins, Character side, Match match) {
        player.incrementBetCount();

        if (player.getBalance() >= bettingCoins) {
            if (match == null) {
                throw new RuntimeException("Match has an empty value!");
            }

            char result = match.getResult();

            if (isWinningBet(side, result)) {
                handleWinningBet(player, bettingCoins, side, match);
            } else if (!isDraw(result)) { //if result is 'D' nothing should happen. Player can only get/lose coins
                handleLosingBet(player, bettingCoins, match);
            }

        } else {
            //Player perform illegal action: insufficient balance for the bet
            player.becomesIllegitimate();
        }
    }

    private boolean isWinningBet(Character side, char result) {
        return Objects.equals(side, result);
    }

    private boolean isDraw(char result) {
        return result == 'D';
    }

    private void handleWinningBet(Player player, long bettingCoins, Character side, Match match) {
        //Player wins the bet
        long payout = calculatePayout(bettingCoins, match.getRate(side));
        player.addToBalance(payout);
        player.incrementWinCount();

        //Casino balance decreases
        this.tempCasinoBalance -= payout;
    }

    private void handleLosingBet(Player player, long bettingCoins, Match match) {
        //Player loses the bet
        player.subtractFromBalance(bettingCoins);

        //Casino balance increases
        this.tempCasinoBalance += bettingCoins;
    }

    private long calculatePayout(long bettingCoins, double rate) {
        return (long) Math.floor(bettingCoins * rate);
    }

    private void withdraw(Player player, long amountOfCoins) {
        if (player.getBalance() >= amountOfCoins) {
            player.subtractFromBalance(amountOfCoins);
        } else {
            //Player perform illegal action: insufficient balance for withdrawal
            player.becomesIllegitimate();
        }
    }
}
