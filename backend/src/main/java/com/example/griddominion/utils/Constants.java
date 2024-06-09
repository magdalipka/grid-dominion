package com.example.griddominion.utils;


public final class Constants {

    /**
     * Constant for eventual experienceToLevelUp adjustment.
     * The formula for experienceToLevelUp for current level is:
     * round((currentLevel^1.2)*BASE_EXPERIENCE)
     */
    public static final int BASE_EXPERIENCE = 1000;

    public static final int BASE_CLAN_EXPERIENCE = 10000;

    public static final int MAX_USER_LEVEL = 80;

    public static final int MAX_CLAN_LEVEL = 10;

    public static final int MAX_CLAN_MEMBERS = 30;

    public static final double START_LATITUDE = 50.12489;
    public static final double START_LONGITUDE = 19.75846;
    public static final double DIFF_LATITUDE= 0.001214;
    public static final double DIFF_LONGITUDE = 0.001894;

    public static final int RESOURCE_LIMIT = 1000000000;

    public static final double RESOURCE_BONUS = 0.2;

    public static final int INITIAL_GOLD_COST_GOLD_MINE = 500;
    public static final int INITIAL_WOOD_COST_GOLD_MINE = 200;
    public static final int INITIAL_FOOD_COST_GOLD_MINE = 100;

    public static final int INITIAL_GOLD_COST_LUMBER_MILL = 300;
    public static final int INITIAL_WOOD_COST_LUMBER_MILL = 500;
    public static final int INITIAL_FOOD_COST_LUMBER_MILL = 100;

    public static final int INITIAL_GOLD_COST_FARM = 200;
    public static final int INITIAL_WOOD_COST_FARM = 100;
    public static final int INITIAL_FOOD_COST_FARM = 500;

    public static final double UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER = 1.2;


    public static final int INITIAL_GOLD_COST_TOWER = 275;
    public static final int INITIAL_WOOD_COST_TOWER = 450;
    public static final int INITIAL_FOOD_COST_TOWER = 50;

    public static final double UPGRADE_COST_TOWER_MULTIPLIER = 1.1;

    public static final int BASE_ATTACK_TOWER = 50;
    public static final int BASE_HP_MAX_TOWER = 250;

    public static final int MINION_GOLD_COST = 1000;
    public static final int MINION_WOOD_COST = 400;
    public static final int MINION_FOOD_COST = 2000;
    private Constants() {
        // Prevent instantiation
    }
}
