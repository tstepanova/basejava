package ru.javawebinar.basejava.model;

import java.io.Serializable;

public class Link implements Serializable {
    private static final long serialVersionUID = 1L;

    private String text;
    private String url;

    public Link(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public Link(String text) {
        this(text, null);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (url != null ? !url.equals(link.url) : link.url != null) return false;
        return text.equals(link.text);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return text + (url != null && !url.isEmpty() ? " (" + url + ")" : "");
    }
}
