package pl.cichy.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table( name = "tasks")
public class Task {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Task's descriptions must not be empty")
    //waliduje, ale nie wyswietla w jsonie tej message, daje "", od wyrzucenia standardowej wali.
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    //@AttributeOverrides({
    //            @AttributeOverride(column = @Column(name="updatedOn"), name = "updatedOn")
    //        })
    //Każdy atrybut w ten sposob mozna nadpisac i ponazywac
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    Task(){ }

    public int getId() { return id; }
    void setId(final int id) { this.id = id; }

    public String getDescription() { return description; }
    void setDescription(final String description) { this.description = description; }

    public boolean isDone() { return done; }
    public void setDone(final boolean done) { this.done = done; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(final LocalDateTime deadline) { this.deadline = deadline; }

    public TaskGroup getGroup() { return group; }
    public void setGroup(final TaskGroup group) { this.group = group; }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }


}
