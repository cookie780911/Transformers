package com.kevinhuang.transformer;

public class Transformer {
    public String id;
    public String name;
    public int strength;
    public int intelligence;
    public int speed;
    public int endurance;
    public int rank;
    public int courage;
    public int firepower;
    public int skill;
    public char team;
    public String team_icon;

    public Transformer(String id, String name, int str, int intel, int speed, int end, int rank, int courage, int fp, int skill, char team, String icon) {
        this.id = id;
        this.name = name;
        this.strength = str;
        this.intelligence = intel;
        this.speed = speed;
        this.endurance = end;
        this.rank = rank;
        this.courage = courage;
        this.firepower = fp;
        this.skill = skill;
        this.team = team;
        this.team_icon = icon;
    }
}
