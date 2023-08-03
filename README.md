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
## 命令
```/ctask``` - 打开GUI界面(Player)
```/ctask progress <group> add <player> <task>``` - 为任务组的任务的进度添加1(OP)
```/ctask progress <group> add <player> <task> <int>``` - 为任务组的任务的进度添加进度(OP)
```/ctask progress <group> set <player> <task> <int>``` - 为任务组的任务的进度设置进度(OP)
