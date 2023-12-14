package com.example.taskservice.controller;

import com.example.taskservice.dto.CreateResponse;
import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskResponseDto;
import com.example.taskservice.dto.TaskStatusDto;
import com.example.taskservice.dto.TasksResponseDto;
import com.example.taskservice.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.example.core.entity.enums.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("task/")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Контроллер для управления задачами, " +
        "все операции записи могут производить только авторы задачи, " +
        "исключение изменение статуса, её так же могут совершать назначенные на данные задачу пользователи")
public class TaskController {

    private final TaskServiceImpl taskService;

    @Operation(summary = "Создание задачи", description = "Создает новую задачу и возвращает ее ID")
    @ApiResponse(responseCode = "200", description = "Задача успешно создана")
    @PostMapping("create")
    public ResponseEntity<CreateResponse> createTask(@RequestBody TaskDto taskDto, Authentication authentication) {
        String login = (String) authentication.getPrincipal();
        Long saveId = taskService.createTask(taskDto, login);
        return ResponseEntity.ok(new CreateResponse(saveId));
    }

    @Operation(summary = "Обновление задачи", description = "Обновляет существующую задачу")
    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена")
    @PutMapping("update/{taskId}")
    public ResponseEntity<CreateResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto, Authentication authentication) {
        String login = (String) authentication.getPrincipal();
        Long saveId = taskService.updateTask(taskId, taskDto, login);
        return ResponseEntity.ok(new CreateResponse(saveId));
    }

    @Operation(summary = "Получение задачи по ID", description = "Получает задачу по ее уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Задача найдена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    @GetMapping("get/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long taskId) {
        TaskResponseDto task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Удаление задачи", description = "Удаляет задачу по ее идентификатору")
    @ApiResponse(responseCode = "200", description = "Задача успешно удалена")
    @DeleteMapping("delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId, Authentication authentication) {
        String login = (String) authentication.getPrincipal();
        taskService.deleteTask(taskId, login);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменение статуса задачи", description = "Изменяет статус задачи")
    @ApiResponse(responseCode = "200", description = "Статус задачи успешно изменен")
    @ApiResponse(responseCode = "400", description = "Недопустимый статус задачи")
    @PatchMapping("{taskId}/status")
    public ResponseEntity<?> changeTaskStatus(@PathVariable Long taskId,
                                              @RequestBody TaskStatusDto taskStatusDto,
                                              Authentication authentication) {
        TaskStatus status;
        try {
            status = TaskStatus.valueOf(taskStatusDto.status().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid task status", HttpStatus.BAD_REQUEST);
        }

        String login = (String) authentication.getPrincipal();
        Long id = taskService.changeTaskStatus(taskId, status, login);
        return ResponseEntity.ok(new CreateResponse(id));
    }

    @Operation(summary = "Назначение исполнителя задачи", description = "Назначает пользователя исполнителем задачи")
    @ApiResponse(responseCode = "200", description = "Исполнитель успешно назначен")
    @PatchMapping("{taskId}/assignee")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId, @RequestParam String userLogin, Authentication authentication) {
        String authorLogin = (String) authentication.getPrincipal();
        Long saveId = taskService.assignTask(taskId, userLogin, authorLogin);
        return ResponseEntity.ok(new CreateResponse(saveId));
    }

    @Operation(summary = "Получение задач автора", description = "Получает задачи, созданные конкретным автором")
    @ApiResponse(responseCode = "200", description = "Список задач предоставлен")
    @GetMapping("get/author/{authorId}")
    public ResponseEntity<TasksResponseDto> getAuthorTasks(@PathVariable Long authorId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        List<TaskResponseDto> authorTasks = taskService.getTasksByUserId(authorId, page, size);
        return ResponseEntity.ok(new TasksResponseDto(authorTasks));
    }

    @Operation(summary = "Получение назначенных задач пользователя", description = "Получает задачи, назначенные конкретному пользователю")
    @ApiResponse(responseCode = "200", description = "Список назначенных задач предоставлен")
    @GetMapping("get/assign/{assignId}")
    public ResponseEntity<TasksResponseDto> getAssignTasks(@PathVariable Long assignId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<TaskResponseDto> assignTasks = taskService.getTasksByAssignId(assignId, page, size);
        return ResponseEntity.ok(new TasksResponseDto(assignTasks));
    }
}
