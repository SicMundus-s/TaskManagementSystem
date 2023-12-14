package com.example.taskservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TaskDto(@NotNull String title, @NotNull String description, List<CommentDto> comments) {

}
