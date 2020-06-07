package pl.cichy.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.cichy.model.Task;
import pl.cichy.model.TaskGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TempService {

    @Autowired
    List<String> temp(TaskGroupRepository repository){
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());

    }
}
