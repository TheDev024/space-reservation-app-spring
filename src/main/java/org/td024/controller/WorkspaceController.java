package org.td024.controller;

import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.td024.entity.Workspace;
import org.td024.exception.WorkspaceSaveFailed;
import org.td024.model.CreateWorkspace;
import org.td024.service.WorkspaceService;

@Controller
@RequestMapping("/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/create")
    public String create(Model model, @ModelAttribute @Valid CreateWorkspace createWorkspace) {
        int id = workspaceService.createWorkspace(modelToWorkspace(createWorkspace));
        if (id != -1) model.addAttribute("success", "Workspace created successfully! ID: " + id);
        else model.addAttribute("error", "Workspace creation failed!");
        return "redirect:/admin";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("editWorkspace") @Valid CreateWorkspace createWorkspace, @PathVariable("id") int id) throws WorkspaceSaveFailed {
        workspaceService.editWorkspace(id, modelToWorkspace(createWorkspace));
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        workspaceService.deleteWorkspace(id);
        return "redirect:/admin";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleDuplicateNameException(ConstraintViolationException e, Model model) {
        System.out.println(e.getLocalizedMessage());
        model.addAttribute("error", "Workspace with that name already exists!");
        return "redirect:/admin";
    }

    @ExceptionHandler(WorkspaceSaveFailed.class)
    public String handleFailedSaveException(WorkspaceSaveFailed e, Model model) {
        System.out.println(e.getLocalizedMessage());
        model.addAttribute("error", "Couldn't edit workspace!");
        return "redirect:/admin";
    }

//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e, Model model) {
//        System.out.println(e.getLocalizedMessage());
//        model.addAttribute("error", e.getLocalizedMessage());
//        return "redirect:/admin";
//    }

    private Workspace modelToWorkspace(CreateWorkspace createWorkspace) {
        return new Workspace(createWorkspace.getName(), createWorkspace.getAddress(), createWorkspace.getType(), createWorkspace.getPrice());
    }
}
