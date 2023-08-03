package me.coolmagic233.ctask;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {
    private String name;
    private List<Task> tasks = new ArrayList<>();

    public TaskGroup(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(String task) {
        for (Task task1 : tasks) {
            if (task1.getName().equals(task)){
                return task1;
            }
        }
        return null;
    }

}
