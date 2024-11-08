package org.rest.restapp.services;

import lombok.RequiredArgsConstructor;
import org.rest.restapp.controllers.exceptions.NotFoundException;
import org.rest.restapp.controllers.exceptions.ResourceAlreadyExistsException;
import org.rest.restapp.models.TaskStatus;
import org.rest.restapp.models.ViewTaskDTO;
import org.rest.restapp.models.TaskDTO;
import org.rest.restapp.services.listeners.UserRemoveEmitter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ConcurrentHashMap<Integer, TaskDTO> tasks = new ConcurrentHashMap<>();
    private final UserService userService;

    @EventListener
    public void handleUserRemoved(UserRemoveEmitter event) {
        tasks.forEach((taskId, task) -> task.removeUsers(event.removedUserIds()));
    }

    @Override
    public List<TaskDTO> getTasks(ViewTaskDTO taskFilter, Integer page, Integer size) {
        return tasks.values().stream()
                .filter(task -> (taskFilter.getTitle() == null || taskFilter.getTitle().equals(task.getTitle())) &&
                        (taskFilter.getDescription() == null || taskFilter.getDescription().equals(task.getDescription())) &&
                        (taskFilter.getStatus() == null || taskFilter.getStatus() == task.getStatus()) &&
                        (taskFilter.getCompletionDate() == null || taskFilter.getCompletionDate().equals(task.getCompletionDate())) &&
                        (taskFilter.getAssignedUserIds() == null || taskFilter.getAssignedUserIds().isEmpty() ||
                                taskFilter.getAssignedUserIds().stream().anyMatch(task.getAssignedUserIds()::contains)))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    @Override
    public TaskDTO getTask(Integer id) {
        if (!tasks.containsKey(id)) {
            throw new NotFoundException("Not found task with id: " + id);
        }
        return tasks.get(id);
    }

    @Override
    public void addTask(ViewTaskDTO task) {
        if (checkExists(task.getTitle())) {
            throw new ResourceAlreadyExistsException("Task with title: " + task.getTitle() + " already exists");
        }

        if (!userService.usersNotExist(task.getAssignedUserIds())) {
            throw  new NotFoundException("Not found users within ids: " + task.getAssignedUserIds());
        }

        Integer newTaskId = this.tasks.size() + 1;
        TaskDTO taskDTO = TaskDTO.builder()
                .id(newTaskId)
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .completionDate(task.getCompletionDate())
                .assignedUserIds(new HashSet<>(task.getAssignedUserIds())) // assign users by ids
                .build();
        tasks.put(newTaskId, taskDTO);
    }

    @Override
    public void removeTask(Integer id) {
        if (!tasks.containsKey(id)) {
            throw new NotFoundException("Not found task with id: " + id);
        }
        tasks.remove(id);
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void assignUsers(Integer taskId, List<Integer> userIds) {
        if (!userService.usersNotExist(userIds)) {
            throw new NotFoundException("Not found users within ids: " + userIds);
        }

        if (!tasks.containsKey(taskId)) {
            throw new NotFoundException(" Not found task with id: " + taskId);
        }

        TaskDTO task = tasks.get(taskId);
        task.addUsers(userIds);
        tasks.put(taskId, task);
    }

    @Override
    public void removeUsers(Integer taskId, List<Integer> userIds) {
        if (!userService.usersNotExist(userIds)) {
            throw new NotFoundException("Not found users within ids: " + userIds);
        }

        if (!tasks.containsKey(taskId)) {
            throw new NotFoundException(" Not found task with id: " + taskId);
        }

        TaskDTO task = tasks.get(taskId);
        task.removeUsers(userIds);
        tasks.put(taskId, task);
    }

    @Override
    public void clearAssignedUsers(Integer taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new NotFoundException(" Not found task with id: " + taskId);
        }

        TaskDTO task = tasks.get(taskId);
        task.clearUsers();
        tasks.put(taskId, task);
    }

    @Override
    public void editTask(Integer id, ViewTaskDTO task) {

        if (!tasks.containsKey(id)) {
            throw new NotFoundException("Not found task with id: " + id);
        }

        if (task.getAssignedUserIds() != null && !userService.usersNotExist(task.getAssignedUserIds())) {
            throw new NotFoundException("Not found users within ids: " + task.getAssignedUserIds());
        }

        TaskDTO oldTask = tasks.get(id);
        System.out.println(oldTask);

        TaskDTO taskDTO = TaskDTO.builder()
                .id(id)
                .title(Optional.ofNullable(task.getTitle()).orElseGet(oldTask::getTitle))
                .description(Optional.ofNullable(task.getDescription()).orElseGet(oldTask::getDescription))
                .status(Optional.ofNullable(task.getStatus()).orElseGet(oldTask::getStatus))
                .completionDate(Optional.ofNullable(task.getCompletionDate()).orElseGet(oldTask::getCompletionDate))
                .assignedUserIds(task.getAssignedUserIds() != null ? new HashSet<>(task.getAssignedUserIds()) : oldTask.getAssignedUserIds()) // assign users by ids
                .build();
        tasks.put(id, taskDTO);
    }

    @Override
    public void updateTaskStatus(Integer taskId, TaskStatus status) {
        if (!tasks.containsKey(taskId)) {
            throw new NotFoundException("Not found task with id: " + taskId);
        }

        TaskDTO task = tasks.get(taskId);
        task.setStatus(status);
        tasks.put(taskId, task);
    }

    private boolean checkExists(String title) {
        return this.tasks.values().stream()
                .anyMatch(task -> task.getTitle().equals(title));
    }

}
