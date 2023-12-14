package com.example.taskservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TaskResponseDto(@NotNull String title,
                              @NotNull String description,
                              List<CommentDto> comments,
                              TaskStatusDto taskStatusDto,
                              String author,
                              String assignee) {
}
