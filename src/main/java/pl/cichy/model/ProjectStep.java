package pl.cichy.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table( name = "project_steps")
public class ProjectStep {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Project step's descriptions must not be empty")
    private String description;
    //klucz obcy do projects
    private int daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public int getId() { return id; }
    public void setId(final int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(final String description) { this.description = description; }

    public int getDaysToDeadline() { return daysToDeadline; }
    public void setDaysToDeadline(final int daysToDeadline) { this.daysToDeadline = daysToDeadline; }

    public Project getProject() { return project; }
    public void setProject(final Project project) { this.project = project; }
}
