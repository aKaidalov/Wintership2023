package task.domain;

import java.util.*;

public class Player {

    private UUID playerId;
    private long balance;
    private List<PlayerAction> actions;
    private boolean isLegitimate;

    public Player(UUID playerId) {
        this.playerId = playerId;
        this.balance = 0L;
        this.actions = new ArrayList<>();

        this.isLegitimate = true;
    }

    public void addPlayerAction(PlayerAction action) {
        this.actions.add(action);
    }

    //playerId
    public UUID getPlayerId() {
        return playerId;
    }

    //balance
    public long getBalance() {
        return balance;
    }

    //actions
    public List<PlayerAction> getActions() {
        return actions;
    }

    //isLegitimate
    public boolean isLegitimate() {
        return isLegitimate;
    }

    public void isNotLegitimate() {
        this.isLegitimate = false;
    }

    //PLAYER PROCESS ACTION
    public void processAction(PlayerAction action, Match match) {
        ActionType actionType = action.getActionType();
        switch (actionType) {
            case DEPOSIT:
                deposit(action.getCoins());
                break;
            case BET:
                bet(action.getCoins(), action.getSide(), match);
                break;
            case WITHDRAW:
                withdraw(action.getCoins());
                break;
            default:
                throw new RuntimeException("Unsupported action type: " + actionType);
        }
    }

    private void deposit(long amountOfCoins) {
        balance += amountOfCoins;
    }

    private void bet(long bettingCoins, Character side, Match match) {
        if (balance >= bettingCoins) {
            if (match != null) { //TODO: what happens if match == null???????

                char result = match.getResult();

                if ((side == 'A' && result == 'A') || (side == 'B' && result == 'B')) { //TODO: side == result????
                    //Player wins the bet
                    balance += (long) Math.floor(bettingCoins * match.getRate(side));
                } else if (result == 'D') {
                    //Match is a draw, return the bet amount //TODO: 1. decide what to do with 'D' | 2. What should it do with balance?
                } else {
                    //Player loses the bet
                    balance -= bettingCoins;
                }
            }
        } else {
            //Player perform illegal action: insufficient balance for the bet
            this.isNotLegitimate();
        }
    }

    private void withdraw(long amountOfCoins) {
        if (balance >= amountOfCoins) {
            balance -= amountOfCoins;
        } else {
            //Player perform illegal action: insufficient balance for withdrawal
            this.isNotLegitimate();
        }
    }


    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", balance=" + balance +
                ", actions=" + actions +
                '}';
    }
}
