package com.example.taskservice.service;

import com.example.core.entity.enums.TaskStatus;
import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    Long createTask(TaskDto taskDto, String login);
    Long updateTask(Long taskId, TaskDto taskDto, String login);
    TaskResponseDto getTaskById(Long taskId);
    void deleteTask(Long taskId, String login);
    Long changeTaskStatus(Long taskId, TaskStatus status, String login);
    Long assignTask(Long taskId, String userLogin, String authorLogin);
    List<TaskResponseDto> getTasksByUserId(Long userId, int page, int size);
    List<TaskResponseDto> getTasksByAssignId(Long assignId, int page, int size);
}