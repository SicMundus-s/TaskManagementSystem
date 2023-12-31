package com.example.taskservice.repository;

import com.example.core.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAuthorId(Long userId, Pageable pageable);

    Page<Task> findByAssigneeId(Long assignId, Pageable pageable);
}
