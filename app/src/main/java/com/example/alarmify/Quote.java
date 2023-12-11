package com.example.alarmify;

public class Quote {
    private String quoteText;
    private String author;

    public Quote(String author, String quoteText) {
        this.quoteText = quoteText;
        this.author = author;
    }

    // Getters and setters for quoteText, author, and category

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // toString() method to represent the Quote object as a string

    @Override
    public String toString() {
        return "Quote{" +
                "quoteText='" + quoteText + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
