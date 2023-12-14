package com.example.taskservice.mapper;

import com.example.taskservice.dto.CommentDto;
import com.example.taskservice.dto.TaskDto;
import com.example.core.entity.Comment;
import com.example.core.entity.Task;
import com.example.core.entity.User;
import com.example.core.entity.enums.TaskStatus;
import com.example.taskservice.dto.TaskResponseDto;
import com.example.taskservice.dto.TaskStatusDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {
    public Task mapToEntity(TaskDto taskDto, User author) {
        Task task = new Task();
        task.setAuthor(author);
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(TaskStatus.OPEN);
        task.setComments(taskDto.comments() != null ? mapToEntityComment(taskDto.comments(), task, author)
                : new ArrayList<>());
        return task;
    }

    public List<Comment> mapToEntityComment(List<CommentDto> commentDtos, Task task, User author) {
        List<Comment> comments = new ArrayList<>();
        for (CommentDto commentDto : commentDtos) {
            Comment comment = new Comment();
            comment.setText(commentDto.text());
            comment.setTask(task);
            comment.setAuthor(author);
            comments.add(comment);
        }
        return comments;
    }

    public TaskResponseDto mapToResponseDto(Task task) {
        return new TaskResponseDto(
                task.getTitle(),
                task.getDescription(),
                mapFromEntityComment(task.getComments()),
                new TaskStatusDto(task.getStatus().name()),
                task.getAuthor().getLogin(),
                (task.getAssignee() != null) ? task.getAssignee().getLogin() : null
        );
    }

    public List<CommentDto> mapFromEntityComment(List<Comment> commentList) {
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = new CommentDto(comment.getText());
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }
}
