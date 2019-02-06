package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization extends AbstractSection implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Organization EMPTY = new Organization(Link.EMPTY, Position.EMPTY);

    private Link sectionHeader;
    private List<Position> positions = new ArrayList<>();

    public Organization() {
    }

    public Organization(Link sectionHeader, Position... positions) {
        this(sectionHeader, Arrays.asList(positions));
    }

    public Organization(Link sectionHeader, List<Position> positions) {
        this.sectionHeader = sectionHeader;
        this.positions = positions;
    }

    public Link getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeader(Link sectionHeader) {
        this.sectionHeader = sectionHeader;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!sectionHeader.equals(that.sectionHeader)) return false;
        return positions.equals(that.positions);

    }

    @Override
    public int hashCode() {
        int result = sectionHeader.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String res = sectionHeader.toString();
        for (Position position : positions) {
            res = res + "\n" + position.toString();
        }
        return res;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {

        public static final String PATTERN = "MM/yyyy";

        public static final Position EMPTY = new Position(DateUtil.NOW, DateUtil.NOW, "", "");

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        public LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        public LocalDate endDate;
        public String textHeader;
        public String text;

        public Position() {
        }

        public Position(int startYear, Month startMonth, String textHeader, String text) {
            this(DateUtil.of(startYear, startMonth), DateUtil.NOW, textHeader, text);
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String textHeader, String text) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), textHeader, text);
        }

        public Position(LocalDate startDate, LocalDate endDate, String textHeader, String text) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(textHeader, "textHeader must not be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.textHeader = textHeader;
            this.text = text;
        }

        public static String getPattern() {
            return PATTERN;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
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

            Position position = (Position) o;

            if (!startDate.equals(position.startDate)) return false;
            if (endDate != null ? !endDate.equals(position.endDate) : position.endDate != null) return false;
            if (!textHeader.equals(position.textHeader)) return false;
            return text != null ? text.equals(position.text) : position.text == null;

        }

        @Override
        public int hashCode() {
            int result = startDate.hashCode();
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + textHeader.hashCode();
            result = 31 * result + (text != null ? text.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return DateTimeFormatter.ofPattern(PATTERN).format(startDate) + " - " + DateTimeFormatter.ofPattern(PATTERN).format(endDate) + '\t' + textHeader + (text != null && !text.isEmpty() ? "\n\t\t\t\t\t" + text : "");
        }
    }
}
