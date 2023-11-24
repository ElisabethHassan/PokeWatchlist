package com.example.pokewatchlist;

public class Pokemon {
    private String name;
    private String number;
    private Integer weight;
    private Integer height;
    private Integer baseXP;
    private String move;
    private String ability;

    public Pokemon(String name, String number, Integer weight, Integer height, Integer baseXP, String move, String ability){
        this.name = name;
        this.number = number;
        this.weight = weight;
        this.height = height;
        this.baseXP = baseXP;
        this.move = move;
        this.ability = ability;
    }


    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getBaseXP() {
        return baseXP;
    }

    public String getMove() {
        return move;
    }

    public String getAbility() {
        return ability;
    }
}
