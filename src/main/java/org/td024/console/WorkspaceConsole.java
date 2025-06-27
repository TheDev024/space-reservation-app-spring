package org.td024.console;

import org.springframework.stereotype.Component;
import org.td024.entity.Interval;
import org.td024.entity.Workspace;
import org.td024.exception.DatetimeParseException;
import org.td024.exception.InvalidTimeIntervalException;
import org.td024.service.WorkspaceService;

import java.util.List;
import java.util.Objects;

@Component
public class WorkspaceConsole {
    private final WorkspaceService workspaceService;
    private final IntervalConsole intervalConsole;

    public WorkspaceConsole(WorkspaceService workspaceService, IntervalConsole intervalConsole) {
        this.workspaceService = workspaceService;
        this.intervalConsole = intervalConsole;
    }

    public void listWorkspaces() {
        System.out.println("\n== ALL WORKSPACES ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        printWorkspaces(workspaces);
    }

    public void listAvailableWorkspaces() {
        System.out.println("\n== AVAILABLE WORKSPACES ==\n");

        System.out.println("Enter interval to check");
        Interval interval;

        try {
            interval = intervalConsole.getInterval();
        } catch (InvalidTimeIntervalException | DatetimeParseException e) {
            System.out.println("Invalid interval: " + e.getMessage());
            return;
        }

        List<Workspace> workspaces = workspaceService.getAvailableWorkspaces(interval);
        printWorkspaces(workspaces);
    }

    public void printWorkspaces(List<Workspace> workspaces) {
        workspaces = workspaces.stream().filter(Objects::nonNull).toList();
        if (workspaces.isEmpty()) System.out.println("No workspaces found!");
        else {
            System.out.printf("%3s: %-30s %-10s %s\n", "ID", "Name", "Type", "Price");

            for (Workspace workspace : workspaces)
                System.out.printf("%3d: %-30s %-10s %3.2f\n", workspace.getId(), workspace.getName(), workspace.getType(), workspace.getPrice());
        }
    }
}
