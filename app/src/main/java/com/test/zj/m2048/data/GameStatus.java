package com.test.zj.m2048.data;

import com.test.zj.m2048.utils.ArrayHelper;

import java.util.Set;

/**
 * 存储游戏状态信息
 */

public class GameStatus {
    final static String REG = "@";

    private int level;
    private int score;
    private int[][] items;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[][] getItems() {
        return items;
    }

    public void setItems(int[][] items) {
        this.items = items;
    }

    public void reset(){
        level = 0;
        score = 0;
        items = null;
    }

    public void loadFromString(String values){
        if(values == null || values == ""){
            return;
        }
        String[] l = values.split(REG);
        if(l.length<2){
            return;
        }
        level = Integer.parseInt(l[0]);
        score = Integer.parseInt(l[1]);
        if(l.length==3){
            items = ArrayHelper.StringToArray(l[2]);
        }
    }

    public String toString(){

        StringBuilder builder = new StringBuilder();

        builder.append(level+REG);
        builder.append(score+REG);
        builder.append(ArrayHelper.ArrayToString(items));

        return builder.toString();
    }

    public GameStatus copy(){
        GameStatus gameStatus = new GameStatus();

        gameStatus.setLevel(getLevel());
        gameStatus.setScore(getScore());

        int[][] temps = null;

        if(items != null){
            temps = new int[items.length][items[0].length];

            for(int i = 0;i<items.length;i++){
                for(int j=0;j<items[0].length;j++){
                    temps[i][j] = items[i][j];
                }
            }
        }

        gameStatus.setItems(temps);
        return gameStatus;
    }

}
