package org.spotifumtp37;


import org.spotifumtp37.delegate.SpotifUMUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nWelcome to SpotifUM!");

        SpotifUMUI application = new SpotifUMUI();
        application.run();
    }
}