package com.example.megohike.domain;

public enum HikingLevel{
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard");

    private String level;
    private HikingLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
