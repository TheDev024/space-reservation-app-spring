package org.td024.dto;

import org.td024.entity.Interval;

public class ReservationDto {
    private Long id;
    private String name;
    private Interval interval;
    private NestedDto<Long> workspace;
    private String reserver;

    public ReservationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public NestedDto<Long> getWorkspace() {
        return workspace;
    }

    public void setWorkspace(NestedDto<Long> workspace) {
        this.workspace = workspace;
    }

    public String getReserver() {
        return reserver;
    }

    public void setReserver(String reserver) {
        this.reserver = reserver;
    }
}
