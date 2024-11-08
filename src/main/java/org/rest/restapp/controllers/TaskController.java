package org.rest.restapp.controllers;

import lombok.RequiredArgsConstructor;
import org.rest.restapp.models.TaskDTO;
import org.rest.restapp.models.ViewTaskDTO;
import org.rest.restapp.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("get")
    public List<TaskDTO> getTasks(@RequestBody ViewTaskDTO viewTaskDTO, @RequestParam Integer page, @RequestParam Integer size) {
        return this.taskService.getTasks(viewTaskDTO, page, size);
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable Integer id) {
        return this.taskService.getTask(id);
    }

    @PostMapping
    public ResponseEntity<Void> addTask(@Validated @RequestBody ViewTaskDTO task) {
        this.taskService.addTask(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeTasks() {
        this.taskService.removeTasks();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTask(@PathVariable Integer id) {
        this.taskService.removeTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<Void> assignUsers(@PathVariable Integer taskId, @RequestBody List<Integer> userIds) {
        this.taskService.assignUsers(taskId, userIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{taskId}/remove")
    public ResponseEntity<Void> removeUsers(@PathVariable Integer taskId, @RequestBody List<Integer> userIds) {
        System.out.println(userIds);
        this.taskService.removeUsers(taskId, userIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{taskId}/clear")
    public ResponseEntity<Void> clearAssignedUsers(@PathVariable Integer taskId) {
        this.taskService.clearAssignedUsers(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> editTask(@PathVariable Integer id, @RequestBody ViewTaskDTO task) {
        this.taskService.editTask(id, task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Integer id, @RequestBody TaskDTO task) {
        this.taskService.updateTaskStatus(id, task.getStatus());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
