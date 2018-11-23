package ru.javawebinar.basejava.model;

import java.util.List;

public class ListSection extends AbstractSection {

    private List<String> list;

    public ListSection(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(String text) {
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
