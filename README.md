# CTask
Nukkit上的任务插件
# 开发者
```
for (TaskGroup taskGroup : CTask.plugin.taskGroups) {
                                for (Task task : taskGroup.getTasks()) {
                                    if (task.getName().equals("Task Name")){
                                        CTask.plugin.addProgress(player,task);
                                        break;
                                    }
                                }
                            }
```
