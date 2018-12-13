package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    private List<Organization> list = new ArrayList<>();

    public OrganizationSection() {
    }

    public OrganizationSection(Organization... organizations) {
        this(Arrays.asList(organizations));
    }

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "list must not be null");
        list = organizations;
    }

    public List<Organization> getList() {
        return list;
    }

    public void setList(List<Organization> list) {
        this.list = list;
    }

    public void addList(Organization organization) {
        list.add(organization);
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
        for (Organization organization : list) {
            res = res + organization.toString() + "\n\n";
        }
        return res;
    }
}
