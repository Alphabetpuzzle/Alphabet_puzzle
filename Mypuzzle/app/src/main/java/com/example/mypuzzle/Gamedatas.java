package com.example.mypuzzle;

public class Gamedatas {
    private String num;
    private String gametime;
    private String gamestep;
    private String gamedate;

    public Gamedatas(String num, String gametime, String gamestep, String gamedate) {
        this.num = num;
        this.gametime = gametime;
        this.gamestep = gamestep;
        this.gamedate = gamedate;
    }
    public String getGametime(){
        return gametime;
    }
    public String getNum(){
        return num;
    }
    public void setNum(String num){
        this.num=num;
    }
    public String getGamestep(){
        return gamestep;
    }
    public String getGamedate(){
        return gamedate;
    }
    public void setGametime(String gametime) {
        this.gametime = gametime;
    }

    public void setGamedate(String gamedate) {
        this.gamedate = gamedate;
    }

    public void setGamestep(String gamestep) {
        this.gamestep = gamestep;
    }
}
