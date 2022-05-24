package com.example.project3;

import java.io.Serializable;

public class Words implements Serializable {
    private final long id;
    private final String ru_word;
    private final String en_word;
    private final String description;
    private final String date;
    private final int cnt;

    public Words (long id, String ru_word, String en_word, String description, String date, int cnt) {
        this.id = id;
        this.ru_word = ru_word;
        this.en_word = en_word;
        this.description = description;
        this.date = date;
        this.cnt = cnt;
    }

    public long getId() {
        return id;
    }

    public String getRu_word() {
        return ru_word;
    }

    public String getEn_word() {
        return en_word;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getCnt() {
        return cnt;
    }
}