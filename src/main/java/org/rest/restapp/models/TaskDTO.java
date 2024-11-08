package org.rest.restapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class TaskDTO {

    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime completionDate;
    private Set<Integer> assignedUserIds;

    public synchronized void addUsers(List<Integer> usersIds) {
        assignedUserIds.addAll(usersIds);
    }

    public synchronized void removeUsers(List<Integer> userIds) {
        userIds.forEach(assignedUserIds::remove);
    }

    public synchronized void clearUsers() {
        assignedUserIds.clear();
    }


}

