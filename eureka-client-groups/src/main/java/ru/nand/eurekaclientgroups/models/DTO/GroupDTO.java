package ru.nand.eurekaclientgroups.models.DTO;

public class GroupDTO {
    private String name;

    public GroupDTO(String name) {
        this.name = name;
    }

    public GroupDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
