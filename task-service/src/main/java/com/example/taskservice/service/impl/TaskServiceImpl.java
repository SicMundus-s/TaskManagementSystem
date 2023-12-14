package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskResponseDto;
import com.example.taskservice.exception.AccessDeniedException;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.exception.UserNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.repository.UserRepository;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import com.example.core.entity.Task;
import com.example.core.entity.User;
import com.example.core.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Long createTask(TaskDto taskDto, String login) {
        User user = findUser(login);
        Task task = taskMapper.mapToEntity(taskDto, user);
        return taskRepository.save(task).getId();
    }

    public Long updateTask(Long taskId, TaskDto taskDto, String login) {
        User user = findUser(login);
        Task existingTask = findTask(taskId);
        Task updatedTask = new Task();
        if (isAuthor(existingTask, user.getLogin())) {
            updatedTask = taskMapper.mapToEntity(taskDto, user);
            updatedTask.setId(existingTask.getId());
        }

        return taskRepository.save(updatedTask).getId();
    }

    public TaskResponseDto getTaskById(Long taskId) {
        Task task = findTask(taskId);
        return taskMapper.mapToResponseDto(task);
    }

    public void deleteTask(Long taskId, String login) {
        User user = findUser(login);
        Task existingTask = findTask(taskId);

        isAuthor(existingTask, user.getLogin());

        taskRepository.delete(existingTask);
    }

    public Long changeTaskStatus(Long taskId, TaskStatus status, String login) {
        User user = findUser(login);
        Task existingTask = findTask(taskId);

        if (!(existingTask.getAuthor().getLogin().equals(user.getLogin())
            || existingTask.getAssignee().getLogin().equals(user.getLogin()))) {
            throw new AccessDeniedException("User " + login + " does not have permission to edit this task");
        }
        existingTask.setStatus(status);
        return taskRepository.save(existingTask).getId();
    }

    public Long assignTask(Long taskId, String userLogin, String authorLogin) {
        User author = findUser(authorLogin);
        User assignUser = findUser(userLogin);
        Task existingTask = findTask(taskId);

        if (isAuthor(existingTask, author.getLogin())) {
            existingTask.setAssignee(assignUser);
        }
        return taskRepository.save(existingTask).getId();
    }

    public List<TaskResponseDto> getTasksByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findByAuthorId(userId, pageable);
        return tasks.stream()
                .map(taskMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDto> getTasksByAssignId(Long assignId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findByAssigneeId(assignId, pageable);
        return tasks.stream()
                .map(taskMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private User findUser(String login) {
        return userRepository.getByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("No user found with login: " + login));
    }

    private boolean isAuthor(Task task, String login) {
        boolean b = task.getAuthor().getLogin().equals(login);
        if(!b) {
            throw new AccessDeniedException("User " + login + " does not have permission to edit this task");
        }
        return true;
    }

    private Task findTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("No task found with id: " + taskId));
    }
}
