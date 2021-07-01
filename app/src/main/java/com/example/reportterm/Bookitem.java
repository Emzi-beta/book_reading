package com.example.reportterm;

public class Bookitem {
    String name;
    String anthor;
    String contents;
    int resId;


    public Bookitem(String name, String anthor) {
        this.name = name;
        this.anthor = anthor;
    }

    public Bookitem(String name, String anthor, String contents, int resId) {
        this.name = name;
        this.anthor = anthor;
        this.contents = contents;
        this.resId = resId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getAnthor() {
        return anthor;
    }

    public void setAnthor(String anthor) {
        this.anthor = anthor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
