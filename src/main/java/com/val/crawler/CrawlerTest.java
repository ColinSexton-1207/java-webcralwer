package com.val.crawler;

public class CrawlerTest {
    public static void main(String[] args) {
        Crawler crawler = new Crawler();

        crawler.search("https://it.illinoisstate.edu", "Laurie Helms");
    }
}
