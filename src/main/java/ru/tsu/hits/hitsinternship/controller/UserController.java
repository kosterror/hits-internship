package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.PaginationResponse;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.service.UserService;
import ru.tsu.hits.hitsinternship.util.SecurityUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить пользователя по id",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PreAuthorize("hasAnyRole('CURATOR', 'DEAN_OFFICER', 'COMPANY_OFFICER')")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Получить информацию о себе",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public UserDto getUser() {
        return userService.getUserById(SecurityUtil.extractId());
    }

    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @PutMapping("/{id}/roles")
    @Operation(summary = "Изменить роли пользователя",
            security = @SecurityRequirement(name = "BearerAuth"))
    public UserDto updateUserRoles(@PathVariable UUID id, @RequestBody Set<Role> roles) {
        return userService.updateUserRoles(id, roles);
    }

    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @PutMapping("/{id}/group")
    @Operation(summary = "Изменить группу пользователя",
            security = @SecurityRequirement(name = "BearerAuth"))
    public UserDto updateUserGroup(@PathVariable UUID id, @RequestBody UUID groupId) {
        return userService.updateUserGroup(id, groupId);
    }

    @Operation(summary = "Получить список всех пользователей",
            security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/all")
    public PaginationResponse<UserDto> getUsers(@RequestParam(required = false) String fullName,
                                                @RequestParam(required = false) Boolean isActive,
                                                @RequestParam(required = false) List<Role> roles,
                                                @RequestParam(required = false) List<UUID> groupIds,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "50") int size) {
        return userService.getUsers(fullName, isActive, roles, groupIds, page, size);
    }


    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Изменить пароль", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestParam String password) {
        userService.changePassword(SecurityUtil.extractId(), password);
        return ResponseEntity.noContent().build();
    }

}
