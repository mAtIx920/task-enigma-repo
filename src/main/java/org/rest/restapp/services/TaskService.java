package org.rest.restapp.services;

import org.rest.restapp.models.TaskDTO;
import org.rest.restapp.models.TaskStatus;
import org.rest.restapp.models.ViewTaskDTO;

import java.util.List;

public interface TaskService {

    void addTask(ViewTaskDTO task);

    List<TaskDTO> getTasks(ViewTaskDTO taskFilter, Integer page, Integer size);

    TaskDTO getTask(Integer id);

    void removeTask(Integer id);

    void removeTasks();

    void assignUsers(Integer taskId, List<Integer> userIds);

    void removeUsers(Integer taskId, List<Integer> userIds);

    void clearAssignedUsers(Integer taskId);

    void editTask(Integer id, ViewTaskDTO task);

    void updateTaskStatus(Integer id, TaskStatus status);

}
