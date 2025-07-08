package org.td024.entity;

import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_workspace"))
    private Workspace workspace;

    @Embedded
    private Interval interval;

    protected Reservation() {
    }

    public Reservation(String name, Workspace workspace, Interval interval) {
        this.name = name;
        this.workspace = workspace;
        this.interval = interval;
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

    public Workspace getWorkspace() {
        return workspace;
    }

    public Interval getInterval() {
        return interval;
    }
}
