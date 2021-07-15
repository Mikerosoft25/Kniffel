package com.kniffel.player;

import java.util.Arrays;

public class Player {
    private String name;
    private int id;
    private int gameId;
    private boolean host;
    private Integer[] points = new Integer[19];
    private Integer place;

    public Player() {
        points[6] = 0;
        points[7] = 0;
        points[8] = 0;

        points[16] = 0;
        points[17] = 0;
        points[18] = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public Integer[] getPoints() {
        return points;
    }

    public void setPoints(Integer[] points) {
        this.points = points;
    }

    public void setPointAt(int index, Integer points) {
        this.points[index] = points;
    }

    public Integer getPointsAt(int index) {
        return points[index];
    }

    public Integer getTotalPoints() {
        return points[18];
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public void resetPoints(){
        this.place = null;

        Arrays.fill(points, null);

        points[6] = 0;
        points[7] = 0;
        points[8] = 0;
        points[16] = 0;
        points[17] = 0;
        points[18] = 0;
    }

    public void calculateScores() {
        int sumTop = 0;
        int sumBottom = 0;

        for (int i = 0; i < 6; i++) {
            if(points[i] != null) {
                sumTop += points[i];
            }
        }
        points[6] = sumTop;

        if(sumTop >= 63) {
            points[7] = 35;
        }

        points[8] = points[7] + sumTop;

        for (int i = 9; i < 16; i++) {
            if(points[i] != null) {
                sumBottom += points[i];
            }
        }

        points[16] = sumBottom;
        points[17] = points[8];
        points[18] = sumBottom + sumTop;
    }
}
