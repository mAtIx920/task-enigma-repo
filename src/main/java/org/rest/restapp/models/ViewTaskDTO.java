package org.rest.restapp.models;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class ViewTaskDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;
    private TaskStatus status;

    @FutureOrPresent
    private LocalDateTime completionDate;

    private List<Integer> assignedUserIds;

}
