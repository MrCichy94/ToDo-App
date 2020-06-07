package pl.cichy.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//ja decyduje ktore metody maja byc widoczne na zewnątrz, tutaj:
//(przez zdublowanie interfejsu)
public interface TaskRepository {

    List <Task> findAll();

    Page <Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findByDone(@Param("state") boolean done);

    Task save (Task entity);
}
