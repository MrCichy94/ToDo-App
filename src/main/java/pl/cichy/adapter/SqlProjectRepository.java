package pl.cichy.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.cichy.model.Project;
import pl.cichy.model.ProjectRepository;
import pl.cichy.model.TaskGroup;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();
}
