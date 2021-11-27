package com.kniffel.player;

import java.util.Arrays;

public class Player {
    private String name;
    private int id;
    private int gameId;
    private boolean host;
    private Integer[] points = new Integer[19];
    private Integer place;
    private String sessionId;
    private boolean connected = true;

    public Player() {
        this.points[6] = 0;
        this.points[7] = 0;
        this.points[8] = 0;

        this.points[16] = 0;
        this.points[17] = 0;
        this.points[18] = 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return this.gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public boolean isHost() {
        return this.host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public Integer[] getPoints() {
        return this.points;
    }

    public void setPoints(Integer[] points) {
        this.points = points;
    }

    public void setPointAt(int index, Integer points) {
        this.points[index] = points;
    }

    public Integer getPointsAt(int index) {
        return this.points[index];
    }

    public Integer getTotalPoints() {
        return this.points[18];
    }

    public Integer getPlace() {
        return this.place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void resetPoints(){
        this.place = null;

        Arrays.fill(points, null);

        this.points[6] = 0;
        this.points[7] = 0;
        this.points[8] = 0;
        this.points[16] = 0;
        this.points[17] = 0;
        this.points[18] = 0;
    }

    public void calculateScores() {
        int sumTop = 0;
        int sumBottom = 0;

        for (int i = 0; i < 6; i++) {
            if(this.points[i] != null) {
                sumTop += this.points[i];
            }
        }
        this.points[6] = sumTop;

        if(sumTop >= 63) {
            this.points[7] = 35;
        }

        this.points[8] = this.points[7] + sumTop;

        for (int i = 9; i < 16; i++) {
            if(this.points[i] != null) {
                sumBottom += this.points[i];
            }
        }

        this.points[16] = sumBottom;
        this.points[17] = this.points[8];
        this.points[18] = this.points[16] + this.points[17];
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + this.name + '\'' +
                ", id=" + this.id +
                ", gameId=" + this.gameId +
                ", host=" + this.host +
                ", points=" + Arrays.toString(this.points) +
                ", place=" + this.place +
                ", sessionId='" + this.sessionId + '\'' +
                ", connected=" + this.connected +
                '}';
    }
}
