package pl.cichy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.cichy.model.Task;
import pl.cichy.model.TaskGroup;
import pl.cichy.model.TaskRepository;


import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

@Configuration
class TestConfiguration {

    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource(){
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa","");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
                            //1) @Primary -dzięki temu w testach to ma priorytet,
                            //jest główne, ale to psuje nam inne testy zależne od repo nie z pamieci
                            //2) @ConditionalOnMissingBean -to na dole obowiazuje
                            //ale tylko wtedy gdy nie ma innego zdefiniowanego taskRepository, ale to tez nic nie daje nam tu
    @Primary
    @Profile("integration") //3) TO załatwia temat, bo tylko gdy odpalimy nasz program z profilem
                            //integration, wywoła nam TaskRepository to ponizsze
                            //w przeciwnym wypadku wywołuje się standardowa konfi (bean)
    TaskRepository testRepo() {
        return new TaskRepository() {

            private Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll () {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll (Pageable page){
                return null;
            }

            @Override
            public Optional<Task> findById (Integer id){
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public boolean existsById (Integer id){
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id (Integer groupId){
                return false;
            }

            @Override
            public List<Task> findByDone ( boolean done){
                return null;
            }

            @Override
            public Task save (Task entity){
                int key = tasks.size() + 1;
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                tasks.put(key, entity);
                return tasks.get(key);
            }
        };
    }
}
