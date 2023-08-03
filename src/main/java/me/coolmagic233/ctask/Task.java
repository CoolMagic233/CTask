package me.coolmagic233.ctask;


import java.util.List;

public class Task{

    private String name;
    private String display;
    private int totalProgress;
    private boolean auto_finish;
    private String finish_message;
    private List<String> cmds;

    public Task(String name, String display, int totalProgress, boolean auto_finish, String finish_message, List<String> cmds) {
        this.name = name;
        this.display = display;
        this.totalProgress = totalProgress;
        this.auto_finish = auto_finish;
        this.finish_message = finish_message;
        this.cmds = cmds;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

    public int getTotalProgress() {
        return totalProgress;
    }

    public boolean isAuto_finish() {
        return auto_finish;
    }

    public String getFinish_message() {
        return finish_message;
    }

    public List<String> getCmds() {
        return cmds;
    }
}
