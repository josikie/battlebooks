package com.josikie.b.battlebooks;

public class Books {
    private String image;
    private String bookTitle;
    private String authorName;
    private String url;

    public String getUrl() {
        return url;
    }

    public Books(String image, String bookTitle, String authorName, String url) {
        this.image = image;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.url = url;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getImage() {
        return image;
    }
}
