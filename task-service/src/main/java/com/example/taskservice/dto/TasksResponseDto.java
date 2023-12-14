package com.example.taskservice.dto;

import java.util.List;

public record TasksResponseDto(List<TaskResponseDto> taskResponseDtoList) {
}
