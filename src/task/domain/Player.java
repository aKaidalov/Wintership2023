package task.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Player {

    private final UUID playerId;
    private long balance;
    private List<PlayerAction> actions;
    private boolean isLegitimate;
    private BigDecimal winRate;
    private int betCount = 0;
    private int winCount = 0;

    private PlayerAction firstIllegalAction;

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
    public void setBalance(long balance) {
        this.balance = balance;
    }
    public void addToBalance(long balance) {
        this.balance += balance;
    }
    public void subtractFromBalance(long balance) {
        this.balance -= balance;
    }

    //actions
    public List<PlayerAction> getActions() {
        return actions;
    }

    //isLegitimate
    public boolean isLegitimate() {
        return isLegitimate;
    }

    public void becomesIllegitimate() {
        this.isLegitimate = false;
    }

    //winRate
    public BigDecimal getWinRate() {
        return winRate;
    }
    public void calculateWinRate() {
        if (betCount == 0) {
            this.winRate = BigDecimal.ZERO;
        } else {
            this.winRate = new BigDecimal(winCount).divide(new BigDecimal(betCount), 2, RoundingMode.HALF_DOWN);
        }
    }

    //betCount
    public void incrementBetCount() {
        this.betCount++;
    }
    //winCount
    public void incrementWinCount() {
        this.winCount++;
    }

    //firstIllegalAction
    public PlayerAction getFirstIllegalAction() {
        return firstIllegalAction;
    }
    public void setFirstIllegalAction(PlayerAction action) {
        this.firstIllegalAction = action;
    }


    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", balance=" + balance +
                ", isLegitimate=" + isLegitimate +
                ", winRate=" + winRate +
                '}';
    }
}
