# CTask
Nukkit上的任务插件
# 开发者
## 调用方法
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
## Maven项目
```
<dependency>
  <groupId>me.coolmagic233.ctask</groupId>
  <artifactId>CTask</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
