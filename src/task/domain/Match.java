package task.domain;

import java.util.*;

public class Match {
    private UUID matchId;
    private double rateA;
    private double rateB;
    private char result;


    public Match(UUID matchId, double rateA, double rateB, char result) {
        this.matchId = matchId;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = result;
    }


    //matchId
    public UUID getMatchId() {
        return matchId;
    }

    //rate
    public double getRate(Character side) {

        if (side == null) {
            throw new IllegalArgumentException("Side cannot be null.");
        } else if (Objects.equals(side, 'A')) {
            return rateA;
        } else if (Objects.equals(side, 'B')) {
            return rateB;
        } else {
            throw new IllegalArgumentException("Illegal argument: " + side + ". Must be 'A' or 'B'.");
        }
    }

    //result
    public char getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", rateA=" + rateA +
                ", rateB=" + rateB +
                ", result=" + result +
                '}';
    }
}
