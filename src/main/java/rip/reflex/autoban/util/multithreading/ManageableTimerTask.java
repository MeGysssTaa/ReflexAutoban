/*
 * Copyright 2021 German Vekhorev (DarksideCode)
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

import java.util.TimerTask;

/**
 * An extended version of Java's pure TimerTask to
 * enable us better control over it.
 */
public abstract class ManageableTimerTask extends TimerTask {

    /**
     * Register the task as running when its being created.
     */
    public ManageableTimerTask() {
        TaskManager.registerRunnnig(this);
    }

    /**
     * Remove this task from the currently running tasks set.
     */
    public void unregister() {
        TaskManager.unregister(this);
    }

    /**
     * Override the default cancel method with additional
     * code inserted before the main, for unregistering the task.
     */
    @Override
    public boolean cancel() {
        unregister();
        return super.cancel();
    }

}
