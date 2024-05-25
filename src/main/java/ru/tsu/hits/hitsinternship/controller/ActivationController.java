package ru.tsu.hits.hitsinternship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.service.AuthService;

import java.util.UUID;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ActivationController {

    private final AuthService authService;

    @GetMapping("/activate/{id}")
    public String activate(@PathVariable UUID id, Model model) {
        if (!authService.canUserActivateAccount(id)) {
            return "cannot-activate-page";
        }

        model.addAttribute("userId", id);
        return "activation-page";
    }

    @PostMapping("/activate")
    public String processActivation(@RequestParam UUID userId, @RequestParam String password) {
        try {
            authService.activateUser(userId, password);
        } catch (ConflictException e) {
            return "cannot-activate-page";
        }
        return "activated-page";
    }

}
