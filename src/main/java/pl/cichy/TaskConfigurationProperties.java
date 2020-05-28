package pl.cichy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {

    private boolean allowMultipleTasksFromTemplate;

    public boolean isAllowMultipleTasksFromTemplate() {
        return allowMultipleTasksFromTemplate;
    }

    public void setAllowMultipleTasksFromTemplate(final boolean allowMultipleTasksFromTemplate) {
        this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
    }

    @Configuration
    @ConfigurationProperties("task.template")
    public static class template {
        private static boolean allowMultipleTasks;

        public static boolean isAllowMultipleTasks() {
            return allowMultipleTasks;
        }

        public void setAllowMultipleTasks(final boolean allowMultipleTasks) {
            this.allowMultipleTasks = allowMultipleTasks;
        }

        public boolean getAllowMultipleTasks(){
            return this.allowMultipleTasks;
        }
    }

}


