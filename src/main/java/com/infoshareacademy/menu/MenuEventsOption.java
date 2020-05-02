package com.infoshareacademy.menu;

public enum MenuEventsOption {
    RETURN("Powrót do głównego menu"),
    ALL("Pokaż wszystkie wydarzenia"),
    COMING("Pokaż nadchodzące wydarzenia"),
    FILTER("Filtruj wydarzenia"),
    SEARCH("Wyszukaj wydarzenia");

    private String description;

    MenuEventsOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

