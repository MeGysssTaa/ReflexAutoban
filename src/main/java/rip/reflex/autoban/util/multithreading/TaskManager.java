/*
 * Copyright 2020 DarksideCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rip.reflex.autoban.util.multithreading;

import rip.reflex.autoban.ReflexAutoban;

import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

/**
 * Timer, TimerTask, Thread and similar stuff management utilities.
 */
public class TaskManager {

    /**
     * Set of timer tasks running at the moment.
     */
    private static final Set<TimerTask> running = new HashSet<>();

    /**
     * Check if the given TimerTask is running at the moment.
     *
     * @param task The timer task to check.
     * @return true if the given TimerTask is running at the moment, false otherwise.
     */
    public static boolean isRunning(final TimerTask task) {
        return running.contains(task);
    }

    /**
     * Register the given TimerTask as currently running.
     * @param task The timer task to register.
     */
    public static void registerRunnnig(final TimerTask task) {
        if (!(running.contains(task))) running.add(task);
    }

    /**
     * Remove the given TimerTask from the currently running tasks set.
     * @param task The timer task to unregister.
     */
    public static void unregister(final TimerTask task) {
        running.remove(task);
    }

    /**
     * Execute the given block of code asynchronously.
     * @param r The code to execute.
     */
    public static void runAsync(final Runnable r) {
        //Bukkit.getScheduler().runTaskAsynchronously(ReflexAutoban.getInstance(), r);
        ReflexAutoban.getInstance().getExecutor().execute(r);
    }

}
