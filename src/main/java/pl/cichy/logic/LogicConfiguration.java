package pl.cichy.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cichy.TaskConfigurationProperties;
import pl.cichy.model.ProjectRepository;
import pl.cichy.model.TaskGroupRepository;
import pl.cichy.model.TaskRepository;

@Configuration
class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskGroupService taskGroupService,
            final TaskConfigurationProperties config
            ) {
        return new ProjectService(repository, taskGroupRepository, config, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            final TaskRepository taskRepository
            ) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
