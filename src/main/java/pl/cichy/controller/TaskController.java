package pl.cichy.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.cichy.logic.TaskService;
import pl.cichy.model.Task;
import pl.cichy.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final ApplicationEventPublisher eventPublisher;
    private final TaskRepository repository;
    private final TaskService service;

    //rozwiazanie problemu nieunikalnych beanow na potrezby wstawania aplikacji do testów
    //1) rozwiazanie mocno springowe to: @Qualifier("sqlTaskRepository") Wskazanie o ktore miejsce nam chodzi w tworzeniu beana
    //TO ZEPSULOBY NAM TESTY BO BY BRALO INNA REPOSITORY (NIE TEST REPO)
    TaskController(ApplicationEventPublisher eventPublisher, final TaskRepository repository,
                   final TaskService service) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    //Dłuższa wersja też mogłaby być w przypadku GetMapping'ów
    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")
    //Poniżej wyłączamy spod parametrów sort, page, size (zeby działały z dokumentacji /search)
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    /* ASYNCHRONICZNE WCZYTYWANIE
    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }
     */

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(repository.findByDone(state));
    }

    //na sztywno ustawiamy id, public seter ustawiony do id
    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    //UWAGA- w projektach używamy albo @Transactional do WSZYSTKIEGO, albo metody z ręcznym zapisanem do repozytorium
    //Takiej która jest tutaj użyta w metodzie PutMapping u góry

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(eventPublisher::publishEvent);
        return ResponseEntity.noContent().build();
    }
}

//transactional jest po to żeby nie było byków typu "wysłałeś info" a nie przeszła zmiana stanu na bazie np
