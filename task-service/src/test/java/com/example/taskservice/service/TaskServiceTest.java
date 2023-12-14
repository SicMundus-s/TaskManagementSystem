package com.example.taskservice.service;

import com.example.core.entity.Task;
import com.example.core.entity.User;
import com.example.core.entity.enums.TaskStatus;
import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskResponseDto;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.repository.UserRepository;
import com.example.taskservice.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private User user;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task();
        user = new User();
        task.setId(1L);
        task.setAuthor(user);
        user.setId(1L);
        user.setAuthoredTasks(List.of(task));
        user.setLogin("userLogin@mail.ru");
        taskDto = new TaskDto("Title", "Description", null);
    }

    @Test
    void whenCreateTask_thenTaskIsSaved() {
        given(userRepository.getByLogin(anyString())).willReturn(Optional.of(user));
        given(taskMapper.mapToEntity(any(TaskDto.class), any(User.class))).willReturn(task);
        given(taskRepository.save(any(Task.class))).willReturn(task);

        Long taskId = taskService.createTask(taskDto, "userLogin@mail.ru");

        then(taskRepository).should().save(any(Task.class));
        assertNotNull(taskId);
    }

    @Test
    void whenUpdateTask_thenTaskIsUpdated() {
        given(userRepository.getByLogin(anyString())).willReturn(Optional.of(user));
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(taskMapper.mapToEntity(any(TaskDto.class), any(User.class))).willReturn(task);
        given(taskRepository.save(any(Task.class))).willReturn(task);

        Long taskId = taskService.updateTask(1L, taskDto, "userLogin@mail.ru");

        then(taskRepository).should().save(any(Task.class));
        assertNotNull(taskId);
    }

    @Test
    void whenGetTaskById_thenTaskIsReturned() {
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(taskMapper.mapToResponseDto(any(Task.class))).willReturn(new TaskResponseDto(null,
                null,
                null,
                null,
                user.getLogin(),
                null));

        TaskResponseDto foundTask = taskService.getTaskById(1L);

        then(taskRepository).should().findById(anyLong());
        assertNotNull(foundTask);
    }

    @Test
    void whenDeleteTask_thenTaskIsDeleted() {
        given(userRepository.getByLogin(anyString())).willReturn(Optional.of(user));
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        willDoNothing().given(taskRepository).delete(any(Task.class));

        taskService.deleteTask(1L, "userLogin@mail.ru");

        then(taskRepository).should().delete(any(Task.class));
    }

    @Test
    void whenChangeTaskStatus_thenStatusIsUpdated() {
        given(userRepository.getByLogin(anyString())).willReturn(Optional.of(user));
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(taskRepository.save(any(Task.class))).willReturn(task);

        Long taskId = taskService.changeTaskStatus(1L, TaskStatus.IN_PROGRESS, "userLogin");

        then(taskRepository).should().save(any(Task.class));
        assertEquals(task.getId(), taskId);
    }

    @Test
    void whenAssignTask_thenAssigneeIsUpdated() {
        given(userRepository.getByLogin(anyString())).willReturn(Optional.of(user));
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(taskRepository.save(any(Task.class))).willReturn(task);

        Long taskId = taskService.assignTask(1L, "assigneeLogin", "authorLogin");

        then(taskRepository).should().save(any(Task.class));
        assertEquals(task.getId(), taskId);
    }

    @Test
    void whenGetTasksByUserId_thenTasksAreReturned() {
        Page<Task> page = new PageImpl<>(Collections.singletonList(task));
        given(taskRepository.findByAuthorId(anyLong(), any(Pageable.class))).willReturn(page);
        given(taskMapper.mapToResponseDto(any(Task.class))).willReturn(new TaskResponseDto(null,
                null,
                null,
                null,
                user.getLogin(),
                null));

        List<TaskResponseDto> tasks = taskService.getTasksByUserId(1L, 0, 10);

        then(taskRepository).should().findByAuthorId(anyLong(), any(Pageable.class));
        assertFalse(tasks.isEmpty());
    }

    @Test
    void whenGetTasksByAssignId_thenTasksAreReturned() {
        Page<Task> page = new PageImpl<>(Collections.singletonList(task));
        given(taskRepository.findByAssigneeId(anyLong(), any(Pageable.class))).willReturn(page);
        given(taskMapper.mapToResponseDto(any(Task.class))).willReturn(new TaskResponseDto(null,
                null,
                null,
                null,
                user.getLogin(),
                null));

        List<TaskResponseDto> tasks = taskService.getTasksByAssignId(1L, 0, 10);

        then(taskRepository).should().findByAssigneeId(anyLong(), any(Pageable.class));
        assertFalse(tasks.isEmpty());
    }
}
