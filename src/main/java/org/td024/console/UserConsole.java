package org.td024.console;

import org.springframework.stereotype.Component;
import org.td024.entity.Interval;
import org.td024.entity.Reservation;
import org.td024.entity.Workspace;
import org.td024.exception.DatetimeParseException;
import org.td024.exception.InvalidInputException;
import org.td024.exception.InvalidTimeIntervalException;
import org.td024.service.ReservationService;
import org.td024.service.WorkspaceService;

import java.util.Date;
import java.util.List;

import static org.td024.console.util.ConsoleReader.readInt;
import static org.td024.console.util.ConsoleReader.readLine;

@Component
public class UserConsole {
    private final WorkspaceConsole workspaceConsole;
    private final ReservationConsole reservationConsole;
    private final IntervalConsole intervalConsole;
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public UserConsole(WorkspaceConsole workspaceConsole, ReservationConsole reservationConsole, IntervalConsole intervalConsole, WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceConsole = workspaceConsole;
        this.reservationConsole = reservationConsole;
        this.intervalConsole = intervalConsole;
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    public void menu() {
        System.out.println("\n== Welcome to the USER CONSOLE ==");
        boolean active = true;

        while (active) {
            String option = readLine("\nPlease select an option:\n1 - List available workspaces\n2 - Make reservation\n3 - List my reservations\n4 - Cancel reservation\n\n0 - Back\n\n> ");

            switch (option) {
                case "1":
                    workspaceConsole.listAvailableWorkspaces();
                    break;

                case "2":
                    makeReservation();
                    break;

                case "3":
                    viewMyReservations();
                    break;

                case "4":
                    cancelReservation();
                    break;

                case "0":
                    active = false;
                    break;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void makeReservation() {
        System.out.println("Enter reservation interval");
        Interval interval;

        try {
            interval = intervalConsole.getInterval();
        } catch (InvalidTimeIntervalException | DatetimeParseException e) {
            System.out.println("Invalid interval: " + e.getMessage());
            return;
        }

        if (interval.getStartTime().before(new Date())) {
            System.out.println("Reservation can't be made in the past!");
            return;
        }

        List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(interval);
        if (availableWorkspaces.isEmpty()) {
            System.out.println("No available workspace to reserve!");
            return;
        }

        workspaceConsole.printWorkspaces(availableWorkspaces);
        int spaceId;
        try {
            spaceId = readInt("Enter workspace ID to reserve (0 - Cancel): ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (spaceId == 0) return;
        String name = readLine("Enter reservation name: ");

        reservationService.makeReservation(name, spaceId, interval);
    }

    private void viewMyReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found!");
        }

        System.out.println("\n== MY RESERVATIONS ==\n");
        reservationConsole.printReservations(reservations);
    }

    private void cancelReservation() {
        System.out.println("\n== CANCEL RESERVATION ==\n");
        List<Reservation> reservations = reservationService.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No reservation to cancel!");
            return;
        }
        reservationConsole.printReservations(reservations);

        int id;
        try {
            id = readInt("Enter reservation ID to cancel (0 - Cancel): ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (id == 0) return;

        reservationService.cancelReservation(id);
    }
}
