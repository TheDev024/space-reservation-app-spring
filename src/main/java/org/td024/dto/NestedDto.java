package org.td024.dto;

public class NestedDto<ID> {
    private ID id;
    private String name;

    public NestedDto() {
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
