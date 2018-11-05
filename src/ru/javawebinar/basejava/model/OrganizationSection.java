package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class OrganizationSection extends Section {

    private List<Organization> list = new ArrayList<>();

    public List<Organization> get() {
        return list;
    }

    public void add(Link sectionHeader, String startDate, String endDate, String textHeader, String text) {
        this.list.add(new Organization(sectionHeader, startDate, endDate, textHeader, text));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

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
            res = res + list.get(i) + '\n';
        }
        return res;
    }

    private class Organization extends Section {

        private Link sectionHeader;
        private String startDate;
        private String endDate;
        private String textHeader;
        private String text;

        public Organization(Link sectionHeader, String startDate, String endDate, String textHeader, String text) {
            this.sectionHeader = sectionHeader;
            this.startDate = startDate;
            this.endDate = endDate;
            this.textHeader = textHeader;
            this.text = text;
        }

        public Link getSectionHeader() {
            return sectionHeader;
        }

        public void setSectionHeader(Link sectionHeader) {
            this.sectionHeader = sectionHeader;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getTextHeader() {
            return textHeader;
        }

        public void setTextHeader(String textHeader) {
            this.textHeader = textHeader;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Organization that = (Organization) o;

            if (sectionHeader != null ? !sectionHeader.equals(that.sectionHeader) : that.sectionHeader != null)
                return false;
            if (!startDate.equals(that.startDate)) return false;
            if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
            if (!textHeader.equals(that.textHeader)) return false;
            return text != null ? text.equals(that.text) : that.text == null;
        }

        @Override
        public int hashCode() {
            int result = sectionHeader != null ? sectionHeader.hashCode() : 0;
            result = 31 * result + startDate.hashCode();
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + textHeader.hashCode();
            result = 31 * result + (text != null ? text.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return (sectionHeader != null ? sectionHeader.toString() + '\n' : "") +
                    startDate + " - " +
                    (endDate != null && !endDate.isEmpty() ? endDate : "Сейчас") + '\t' +
                    textHeader +
                    (text != null && !text.isEmpty() ? "\n\t\t\t\t\t" + text : "");
        }
    }
}
