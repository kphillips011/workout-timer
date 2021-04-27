package com.example.workout_timer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RoutineTimer extends Timer {
    // TODO timer functionality for RoutineElement
    Routine routine;
    Timer cumulativeTimer;
    ArrayList<TimerTask> tasks;
    int totalDuration;

    public RoutineTimer(Routine routine) {
        // TODO
        this.routine = routine;
        this.cumulativeTimer = new Timer();
        for (RoutineElement element : routine.getElements()) {
            if (element.getDuration() != null) { totalDuration += element.getDuration(); }
        }
    }

    public void scheduleTasks(ArrayList<TimerTask> tasks) {
        // TODO
        this.tasks = tasks;
        for (TimerTask task : tasks) {
            super.schedule(task, 10);
        }
    }

    public int run() {
        // TODO
        int duration = 0;
        while (duration > 0) {
            for (TimerTask task : this.tasks) {
                duration += task.scheduledExecutionTime();
                task.run();
            }
        }
        // class completed in specified duration, should match totalDuration
        return duration;
    }
}
