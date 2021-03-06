package pl.cichy.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    boolean existsByDoneIsFalseAndProject_Id(Integer groupId);

    TaskGroup save (TaskGroup entity);

    boolean existsByDescription(String description);
}
