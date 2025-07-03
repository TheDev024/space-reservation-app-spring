package org.td024.entity;

import jakarta.persistence.*;
import org.td024.enums.WorkspaceType;

import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}, name = "unique_name_address"))
public class Workspace implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;

    @Enumerated
    @Column(nullable = false, length = 10)
    private WorkspaceType type;

    @Column(nullable = false)
    private BigDecimal price;

    protected Workspace() {
    }

    public Workspace(String name, String address, WorkspaceType type, BigDecimal price) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
