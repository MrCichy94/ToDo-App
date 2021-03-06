package pl.cichy.model;

import org.hibernate.annotations.GenericGenerator;
import pl.cichy.model.event.TaskEvent;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group) {
        this.description = description;
        this.deadline = deadline;
        if (group != null) {
            this.group = group;
        }
    }

    public int getId() { return id; }
    void setId(final int id) { this.id = id; }

    public String getDescription() { return description; }
    void setDescription(final String description) { this.description = description; }

    public boolean isDone() { return done; }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }


    public LocalDateTime getDeadline() { return deadline; }
    void setDeadline(final LocalDateTime deadline) { this.deadline = deadline; }

    TaskGroup getGroup() { return group; }
    void setGroup(final TaskGroup group) { this.group = group; }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }


}
