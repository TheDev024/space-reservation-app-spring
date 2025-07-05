package org.td024.model;

import jakarta.validation.constraints.NotBlank;
import org.td024.entity.Interval;

public class MakeReservation {
    @NotBlank(message = "Name must not be blank")
    private String name;

    private int workspaceId;

    private Interval interval;

    public MakeReservation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(int workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }
}
