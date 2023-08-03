package me.coolmagic233.ctask;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import me.coolmagic233.datamanager.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CTask extends PluginBase implements Listener {
    public Settings settings;
    public static CTask plugin;
    public List<TaskGroup> taskGroups = new ArrayList<>();
    public Map<Player,Boolean> taskStatus = new HashMap<>();
    public Map<Player,Boolean> taskStatusFinish = new HashMap<>();
    public Map<Player,Integer> taskProgress = new HashMap<>();
    public final static int FORM_GROUP = 11451411;
    public final static int FORM_TASK = 11451412;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        settings = new Settings(getConfig());
        for (String taskGroup : getConfig().getStringList("task_group")) {
            List<Task> tasks = new ArrayList<>();
            for (Object value : (getConfig().getList(taskGroup))) {
                HashMap map = (HashMap) value;
                tasks.add(new Task(String.valueOf(map.get("name")),String.valueOf(map.get("display")),Integer.parseInt(String.valueOf(map.get("TotalProgress"))),Boolean.parseBoolean(String.valueOf(map.get("auto_finish"))),String.valueOf(map.get("finish_message")),(List<String>) map.get("finish_cmd")));
                DataManager.getInstance().registerVariable(taskGroup+"_"+map.get("name")+"_status");
                DataManager.getInstance().registerVariable(taskGroup+"_"+map.get("name")+"_progress");
                DataManager.getInstance().registerVariable(taskGroup+"_"+map.get("name")+"_finish");
            }

            TaskGroup tg = new TaskGroup(taskGroup,tasks);
            taskGroups.add(tg);
        }
        getServer().getCommandMap().register("ctask", new Command("ctask","任务系统命令") {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if (strings.length == 0){
                    FormWindowSimple form = new FormWindowSimple(Settings.prefix,"");
                    for (TaskGroup taskGroup : taskGroups) {
                        form.addButton(new ElementButton(taskGroup.getName()+" ["+taskGroup.getTasks().size()+"]"));
                    }
                    if (commandSender.isPlayer()) ((Player) commandSender).showFormWindow(form,FORM_GROUP);
                    return true;
                }
                switch (strings[0]){
                        //ctask progress <group> add <player> <task><5>
                        //ctask progress <group> add <player> <task> <Integer><6>
                        //ctask progress <group> set <player> <task> <Integer><6>
                        case "progress":
                            if (commandSender.isOp()) {
                            if (strings.length == 5){
                                if (strings[2].equals("add")){
                                    Player player = getServer().getPlayer(strings[3]);
                                    if (player == null){
                                        commandSender.sendMessage("该玩家"+strings[3]+"不在线");
                                        return true;
                                    }
                                    TaskGroup taskGroup = null;
                                    for (TaskGroup group : taskGroups) {
                                        if (group.getName().equals(strings[1])){
                                            taskGroup = group;
                                            break;
                                        }
                                    }
                                    if (taskGroup == null){
                                        commandSender.sendMessage("任务组"+strings[1]+"不存在");
                                        return true;
                                    }
                                    Task task = taskGroup.getTask(strings[4]);
                                    if (task == null){
                                        commandSender.sendMessage("该任务组不存在任务"+strings[4]);
                                        return true;
                                    }
                                    taskProgress.put(player,taskProgress.get(player) + 1);
                                    commandSender.sendMessage("操作完成");
                                    checkProgress(player,task);
                                }
                            }
                                if (strings.length == 6){
                                    if (strings[2].equals("add")){
                                        Player player = getServer().getPlayer(strings[3]);
                                        if (player == null){
                                            commandSender.sendMessage("该玩家"+strings[3]+"不在线");
                                            return true;
                                        }
                                        TaskGroup taskGroup = null;
                                        for (TaskGroup group : taskGroups) {
                                            if (group.getName().equals(strings[1])){
                                                taskGroup = group;
                                                break;
                                            }
                                        }
                                        if (taskGroup == null){
                                            commandSender.sendMessage("任务组"+strings[1]+"不存在");
                                            return true;
                                        }
                                        Task task = taskGroup.getTask(strings[4]);
                                        if (task == null){
                                            commandSender.sendMessage("该任务组不存在任务"+strings[4]);
                                            return true;
                                        }
                                        int progress = 0;
                                        try{
                                            progress = Integer.parseInt(strings[5]);
                                        }catch (Exception e){
                                            commandSender.sendMessage(strings[5]+"不是一个数字");
                                            return true;
                                        }
                                        if (progress < 0){
                                            commandSender.sendMessage("任务进度不能小于0");
                                            return true;
                                        }
                                        taskProgress.put(player,taskProgress.get(player) + progress);
                                        commandSender.sendMessage("操作完成");
                                        checkProgress(player,task);
                                    }
                                    if (strings[2].equals("set")){
                                        Player player = getServer().getPlayer(strings[3]);
                                        if (player == null){
                                            commandSender.sendMessage("该玩家"+strings[3]+"不在线");
                                            return true;
                                        }
                                        TaskGroup taskGroup = null;
                                        for (TaskGroup group : taskGroups) {
                                            if (group.getName().equals(strings[1])){
                                                taskGroup = group;
                                                break;
                                            }
                                        }
                                        if (taskGroup == null){
                                            commandSender.sendMessage("任务组"+strings[1]+"不存在");
                                            return true;
                                        }
                                        Task task = taskGroup.getTask(strings[4]);
                                        if (task == null){
                                            commandSender.sendMessage("该任务组不存在任务"+strings[4]);
                                            return true;
                                        }
                                        int progress = 0;
                                        try{
                                            progress = Integer.parseInt(strings[5]);
                                        }catch (Exception e){
                                            commandSender.sendMessage(strings[5]+"不是一个数字");
                                            return true;
                                        }
                                        if (progress < 0){
                                            commandSender.sendMessage("任务进度不能小于0");
                                            return true;
                                        }
                                        taskProgress.put(player,progress);
                                        commandSender.sendMessage("操作完成");
                                        checkProgress(player,task);
                                    }
                                }
                            return true;
                    }
                            commandSender.sendMessage("你没有权限执行此命令");
                            break;
                }
                return false;
            }
        });
        getServer().getPluginManager().registerEvents(this,this);
        getLogger().info("插件已加载");
    }

    @EventHandler
    public void onForm(PlayerFormRespondedEvent e){
        FormResponseSimple response = (FormResponseSimple) e.getResponse();
        FormWindowSimple form = (FormWindowSimple) e.getWindow();
        String status = "进行中";
        if (taskStatus.getOrDefault(e.getPlayer(),false) && taskStatusFinish.getOrDefault(e.getPlayer(),false)) status = "已完成";
        else if (taskStatus.getOrDefault(e.getPlayer(),false) && !taskStatusFinish.getOrDefault(e.getPlayer(),false)) status = "可领取";
        switch (e.getFormID()){
            case FORM_GROUP :
                FormWindowSimple simple = null;
                for (TaskGroup taskGroup : taskGroups) {
                    if (response.getClickedButton().getText().equals(taskGroup.getName()+" ["+taskGroup.getTasks().size()+"]")){
                        simple = new FormWindowSimple(taskGroup.getName(),"");
                        for (Task task : taskGroup.getTasks()) {
                            simple.addButton(new ElementButton(task.getDisplay().replace("@status",status).replace("@progress",String.valueOf(taskProgress.getOrDefault(e.getPlayer(),0))).replace("@totalProgress",String.valueOf(task.getTotalProgress()))));
                        }
                    }

                }
                e.getPlayer().showFormWindow(simple,FORM_TASK);
                break;
            case FORM_TASK:
                for (TaskGroup taskGroup : taskGroups) {
                    if (form.getTitle().equals(taskGroup.getName())){
                        for (Task task : taskGroup.getTasks()) {
                            if (response.getClickedButton().getText().equals(task.getDisplay().replace("@status",status).replace("@progress",String.valueOf(taskProgress.getOrDefault(e.getPlayer(),0))).replace("@totalProgress",String.valueOf(task.getTotalProgress())))){
                                if (taskStatus.getOrDefault(e.getPlayer(),false) && !taskStatusFinish.getOrDefault(e.getPlayer(),false)){
                                    executeCommand(e.getPlayer(),task.getCmds().toArray(),task);
                                    taskStatusFinish.put(e.getPlayer(),true);
                                    getServer().broadcastMessage(task.getFinish_message().replace("@p",e.getPlayer().getName()).replace("@task",task.getName()));
                                    return;
                                }
                                e.getPlayer().sendMessage("你的任务已领取或者仍然进行中...");
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        for (TaskGroup taskGroup : taskGroups) {
            for (Task task : taskGroup.getTasks()) {
                checkProgress(player,task);
                try {
                    taskStatus.put(player,Boolean.valueOf(String.valueOf(DataManager.getInstance().getData(player, taskGroup.getName()+"_"+task.getName()+"_status"))));
                    taskStatusFinish.put(player,Boolean.valueOf(String.valueOf(DataManager.getInstance().getData(player, taskGroup.getName()+"_"+task.getName()+"_finish"))));
                    taskProgress.put(player,Integer.parseInt(String.valueOf(DataManager.getInstance().getData(player, taskGroup.getName()+"_"+task.getName()+"_progress"))));
                } catch (NumberFormatException ex) {
                    DataManager.getInstance().setData(player, taskGroup.getName()+"_"+task.getName()+"_progress",0);
                    DataManager.getInstance().setData(player, taskGroup.getName()+"_"+task.getName()+"_status",false);
                    DataManager.getInstance().setData(player, taskGroup.getName()+"_"+task.getName()+"_finish",false);
                    taskProgress.put(player,0);
                    taskStatus.put(player,false);
                    taskStatusFinish.put(player,false);
                }
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        for (TaskGroup taskGroup : taskGroups) {
            for (Task task : taskGroup.getTasks()) {
               DataManager.getInstance().setData(player, taskGroup.getName()+"_"+task.getName()+"_progress",taskProgress.getOrDefault(player,0));
               DataManager.getInstance().setData(player, taskGroup.getName()+"_"+task.getName()+"_status",taskStatus.getOrDefault(player,false));
               DataManager.getInstance().setData(player, taskGroup.getName()+"_"+task.getName()+"_finish",taskStatusFinish.getOrDefault(player,false));
            }
        }
        taskStatus.remove(player);
        taskStatusFinish.remove(player);
        taskProgress.remove(player);
    }
    public void executeCommand(Player player,String cmd,Task task){
        String taskName = "NaN";
        if (task != null){
            taskName = task.getName();
        }
        if(cmd.split("&")[0].equals("console")){
            getServer().dispatchCommand(new ConsoleCommandSender(),cmd.split("&")[1].replace("@p", player.getName()).replace("@task",taskName));
        }
        if(cmd.split("&")[0].equals("sender")){
            getServer().dispatchCommand(player,cmd.split("&")[1].replace("@p", player.getName()).replace("@task",taskName));
        }
    }
    public void executeCommand(Player player,Object[] cmd,Task task){
        for (Object object : cmd) {
            String s = (String) object;
            String taskName = "NaN";
            if (task != null){
                taskName = task.getName();
            }
            if(s.split("&")[0].equals("console")){
                getServer().dispatchCommand(new ConsoleCommandSender(),s.split("&")[1].replace("@p", player.getName()).replace("@task",taskName));
            }
            if(s.split("&")[0].equals("sender")){
                getServer().dispatchCommand(player,s.split("&")[1].replace("@p", player.getName()).replace("@task",taskName));
            }
        }
    }
    public void checkProgress(Player player,Task task){
       getServer().getScheduler().scheduleDelayedTask(new cn.nukkit.scheduler.Task() {
           @Override
           public void onRun(int i) {
               if (!taskStatus.getOrDefault(player, false)) {
                   if (taskProgress.getOrDefault(player, 0) >= task.getTotalProgress()) {
                       taskProgress.put(player, task.getTotalProgress());
                       taskStatus.put(player,true);
                       player.sendMessage("任务 " + task.getDisplay() + " 已完成,你可以前往任务系统界面领取奖励");
                   }
               }
           }
       },20);
    }

    public void addProgress(Player player, Task task){
        addProgress(player,task,1);
    }

    public void addProgress(Player player, Task task, int progress){
        if (progress < 0) return;
        taskProgress.put(player,taskProgress.get(player)+progress);
        checkProgress(player,task);
    }

    public void setProgress(Player player, Task task, int progress){
        if (progress < 0) return;
        taskProgress.put(player,progress);
        checkProgress(player,task);
    }

}