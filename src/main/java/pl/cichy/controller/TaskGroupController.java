package pl.cichy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.cichy.logic.TaskGroupService;
import pl.cichy.logic.TaskService;
import pl.cichy.model.Task;
import pl.cichy.model.TaskRepository;
import pl.cichy.model.projection.GroupReadModel;
import pl.cichy.model.projection.GroupWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/groups")
class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskRepository repository;
    private final TaskGroupService service;

    //rozwiazanie problemu nieunikalnych beanow na potrezby wstawania aplikacji do testów
    //1) rozwiazanie mocno springowe to: @Qualifier("sqlTaskRepository") Wskazanie o ktore miejsce nam chodzi w tworzeniu beana
    //TO ZEPSULOBY NAM TESTY BO BY BRALO INNA REPOSITORY (NIE TEST REPO)
    TaskGroupController(final TaskRepository repository,
                        final TaskGroupService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate){
        GroupReadModel result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(service.createGroup(toCreate));
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    //UWAGA- w projektach używamy albo @Transactional do WSZYSTKIEGO, albo metody z ręcznym zapisanem do repozytorium
    //Takiej która jest tutaj użyta w metodzie PutMapping u góry

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
