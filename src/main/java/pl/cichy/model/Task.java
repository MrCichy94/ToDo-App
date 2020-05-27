package pl.cichy.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table( name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's descriptions must not be empty")
    //waliduje, ale nie wyswietla w jsonie tej message, daje "", od wyrzucenia standardowej wali.
    private String description;
    private boolean done;

    Task(){ }

    public int getId() { return id; }
    public void setId(int id) { id = id; }

    public String getDescription() { return description; }
    void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }
    void setDone(boolean done) { this.done = done; }


}
