package org.td024.entity;

import jakarta.persistence.*;
import org.td024.auth.entity.AppUser;

import java.time.Instant;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_workspace"))
    private Workspace workspace;

    @Embedded
    private Interval interval;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_reserved_by"))
    private AppUser reservedBy;

    private Instant reservedAt = Instant.now();

    protected Reservation() {
    }

    public Reservation(String name, Workspace workspace, Interval interval, AppUser reservedBy) {
        this.name = name;
        this.workspace = workspace;
        this.interval = interval;
        this.reservedBy = reservedBy;
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

    public AppUser getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(AppUser reservedBy) {
        this.reservedBy = reservedBy;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Instant reservedAt) {
        this.reservedAt = reservedAt;
    }
}
