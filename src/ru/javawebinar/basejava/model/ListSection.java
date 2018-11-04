package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends Section {

    private List<String> list = new ArrayList<>();

    public ListSection(List<String> list) {
        this.list = list;
    }

    public List<String> get() {
        return list;
    }

    public void add(String text) {
        this.list.add(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < list.size(); i++) {
            res = res + "  - " + list.get(i) + '\n';
        }
        return res;
    }
}
