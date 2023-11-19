package task.domain;

import java.util.*;

public class PlayerAction {
    private final ActionType actionType;
    private final UUID matchId;
    private final int coins;
    private final Character side;

    public PlayerAction(ActionType actionType, UUID matchId, int coins, Character side) {
        this.actionType = actionType;
        this.matchId = matchId;
        this.coins = coins;
        this.side = side;
    }


    //actionType
    public ActionType getActionType() {
        return actionType;
    }

    //matchId
    public UUID getMatchId() {
        return matchId;
    }

    //coins
    public int getCoins() {
        return coins;
    }

    //side
    public Character getSide() {
        return side;
    }


    @Override
    public String toString() {
        return "PlayerAction{" +
                "actionType=" + actionType +
                ", matchId=" + matchId +
                ", coins=" + coins +
                ", side=" + side +
                '}';
    }
}
