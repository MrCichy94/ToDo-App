package pl.cichy.adapter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.cichy.model.TaskGroup;
import pl.cichy.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    //Hibernate Query L. HQl
    //daje nam to fakt, że pozostałe metody dalej są dociągane w trybie LAZY, poza tą, zabezpieczamy się przed N+1
    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();


}
