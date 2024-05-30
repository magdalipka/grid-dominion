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

    private Constants() {
        // Prevent instantiation
    }
}
