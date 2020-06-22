package pl.cichy.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.cichy.TaskConfigurationProperties;
import pl.cichy.model.ProjectRepository;
import pl.cichy.model.TaskGroup;
import pl.cichy.model.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        //when

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(),0));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");

    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configuration and no project for a given id")
    void createGroup_configurationOK_And_noProject_throwsIllegalStateException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(),0));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configured allow just 1 group and no groups and projects for a given")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProject_throwsIllegalStateException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(),0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
    }



    private TaskGroupRepository groupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private TaskGroupRepository inMemoryGroupRepository(){
        return new TaskGroupRepository(){

            private int index = 0;
            private Map<Integer,TaskGroup> map = new HashMap<>();

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(group -> !group.isDone())
                        .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
            }

            @Override
            public TaskGroup save(TaskGroup entity) {

                //na to trzeba przymknac oko (przez relflekcje) zeby nie dososowywać api pod testy, bo to byloby glupie
                if(entity.getId() == 0) {
                    try {
                        TaskGroup.class.getDeclaredField("id").set(entity, ++index);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }
        }
    }
}