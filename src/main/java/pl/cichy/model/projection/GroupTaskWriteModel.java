package pl.cichy.model.projection;

import org.springframework.format.annotation.DateTimeFormat;
import pl.cichy.model.Task;
import pl.cichy.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class GroupTaskWriteModel {

    @NotBlank(message = "Task group's descriptions must not be empty")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public String getDescription() { return description; }
    public void setDescription(final String description) { this.description = description; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(final LocalDateTime deadline) { this.deadline = deadline; }

    public Task toTask(TaskGroup group) {
        return new Task(description, deadline, group);
    }

}
