package com.example.android.newsapp;

class Article {
    private final String section;
    private final String title;
    private final String url;

    Article(String section, String title, String url) {
        this.section = section;
        this.title = title;
        this.url = url;
    }

    String getSection() {
        return section;
    }

    String getTitle() {
        return title;
    }

    String getUrl() {
        return url;
    }
}