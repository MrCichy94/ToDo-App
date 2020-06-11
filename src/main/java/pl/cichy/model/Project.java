package pl.cichy.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table( name = "projects")
public class Project {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Project's descriptions must not be empty")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;

    public int getId() { return id; }
    void setId(final int id) { this.id = id; }

    public String getDescription() { return description; }
    void setDescription(final String description) { this.description = description; }

    Set<TaskGroup> getGroups() { return groups; }
    void setGroups(final Set<TaskGroup> groups) { this.groups = groups; }

    public Set<ProjectStep> getSteps() { return steps; }
    void setSteps(final Set<ProjectStep> steps) { this.steps = steps; }
}
