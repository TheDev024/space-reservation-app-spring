package org.td024.console;

import org.springframework.stereotype.Component;
import org.td024.entity.Workspace;
import org.td024.enums.WorkspaceType;
import org.td024.exception.InvalidInputException;
import org.td024.exception.NotFoundException;
import org.td024.exception.WorkspaceIsReservedException;
import org.td024.exception.WorkspaceSaveFailed;
import org.td024.service.WorkspaceService;

import java.math.BigDecimal;
import java.util.List;

import static org.td024.console.util.ConsoleReader.*;

@Component
public class AdminConsole {

    private final WorkspaceService workspaceService;
    private final WorkspaceConsole workspaceConsole;
    private final ReservationConsole reservationConsole;

    public AdminConsole(WorkspaceService workspaceService, WorkspaceConsole workspaceConsole, ReservationConsole reservationConsole) {
        this.workspaceService = workspaceService;
        this.workspaceConsole = workspaceConsole;
        this.reservationConsole = reservationConsole;
    }


    public void menu() {
        System.out.println("\n== Welcome to the ADMIN CONSOLE ==");
        boolean active = true;
        while (active) {
            String option = readLine("\nPlease select an option:\n1 - Create a new workspace\n2 - Edit a workspace\n3 - Delete a workspace\n4 - List all workspaces\n5 - List all available workspaces\n6 - List all reservations\n\n0 - Back\n\n> ");

            switch (option) {
                case "1":
                    createWorkspace();
                    break;

                case "2":
                    editWorkspace();
                    break;

                case "3":
                    deleteWorkspace();
                    break;

                case "4":
                    workspaceConsole.listWorkspaces();
                    break;

                case "5":
                    workspaceConsole.listAvailableWorkspaces();
                    break;

                case "6":
                    reservationConsole.listReservations();
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

    private void createWorkspace() {
        System.out.println("\n== Create a new workspace ==\n");

        String name = readLine("Enter workspace name: ");

        int typeNo;
        try {
            typeNo = readInt("Enter workspace type (1 - OPEN; 2 - PRIVATE; 3 - ROOM): ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }

        WorkspaceType type = getType(typeNo);
        if (type == null) return;

        BigDecimal price;
        try {
            price = readBigDecimal("Enter workspace price: ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }

        Workspace workspace = new Workspace(name, type, price);

        int id = workspaceService.createWorkspace(workspace);
        System.out.println("Workspace created successfully!\nWorkspace ID: " + id);
    }

    private void editWorkspace() {
        System.out.println("\n== Edit a workspace ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("Nothing to edit!");
            return;
        }
        workspaceConsole.printWorkspaces(workspaces);

        int id;

        try {
            id = readInt("Enter workspace ID to edit (0 - Cancel): ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (id == 0) return;

        Workspace workspace;
        try {
            workspace = workspaceService.getWorkspaceById(id);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        String name = workspace.getName();
        name = readLine("Enter new workspace name [" + name + "] (Enter to keep the same): ");
        if (!name.isEmpty()) workspace.setName(name);

        WorkspaceType type = workspace.getType();
        String typeStr = readLine("Enter new workspace type [" + type + "]\n(1 - OPEN; 2 - PRIVATE; 3 - ROOM; Enter to keep the same): ");

        if (!typeStr.isEmpty()) {
            int typeNo = Integer.parseInt(typeStr);
            type = getType(typeNo);
            if (type == null) {
                System.out.println("Invalid workspace type!");
                return;
            }
            workspace.setType(type);
        }

        BigDecimal price = workspace.getPrice();
        String newPrice = readLine("Enter new workspace price [" + price + "] (Enter to keep the same): ");
        if (!newPrice.isEmpty()) workspace.setPrice(new BigDecimal(newPrice));

        try {
            id = workspaceService.editWorkspace(id, workspace);
            System.out.println("Workspace updated successfully! ID: " + id);
        } catch (WorkspaceSaveFailed e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteWorkspace() {
        System.out.println("\n== Delete a workspace ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("Nothing to delete!");
            return;
        }
        workspaceConsole.printWorkspaces(workspaces);

        int id;

        try {
            id = readInt("Enter workspace ID to delete (0 - Cancel): ");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (id == 0) return;

        try {
            boolean deleted = workspaceService.deleteWorkspace(id);
            if (deleted) System.out.println("Workspace deleted successfully!");
            else System.out.println("Workspace not found; ID: " + id);
        } catch (WorkspaceIsReservedException e) {
            System.out.println(e.getMessage());
        }
    }

    private WorkspaceType getType(int typeNo) {
        WorkspaceType type = null;

        switch (typeNo) {
            case 1:
                type = WorkspaceType.OPEN;
                break;
            case 2:
                type = WorkspaceType.PRIVATE;
                break;
            case 3:
                type = WorkspaceType.ROOM;
                break;

            default:
                System.out.println("Invalid workspace type number: " + typeNo);
                break;
        }

        return type;
    }
}
