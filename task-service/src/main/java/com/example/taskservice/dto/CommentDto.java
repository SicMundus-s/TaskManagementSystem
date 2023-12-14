package com.example.taskservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public record CommentDto(@NotNull String text) {
}
