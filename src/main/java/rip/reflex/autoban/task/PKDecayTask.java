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

package rip.reflex.autoban.task;

import org.bukkit.Bukkit;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Stats;

public class PKDecayTask implements Runnable {

    @Override
    public void run() {
        int penalty = ReflexAutoban.getInstance().getSetting("pk_decay.penalty", 2) /* minutes */ * 60 /* seconds */ * 1000 /* milliseconds */;
        Bukkit.getOnlinePlayers().forEach(p -> {
            final Stats stats = Stats.of(p);

            if (stats.getPkTracker().hasPassed(penalty))
                stats.subtractPK();
        });
    }

}
