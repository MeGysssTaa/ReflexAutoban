/*
 * Copyright 2018 DarksideCode
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

package rip.reflex.autoban.bananimation.impl;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rip.reflex.autoban.bananimation.AnimationType;
import rip.reflex.autoban.bananimation.BanAnimation;
import rip.reflex.autoban.util.multithreading.ManageableTimerTask;
import rip.reflex.autoban.util.multithreading.TaskManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Blow a massive hurricane of light white particles around
 * player, increasing their count for 2 seconds of run, until
 * full player evaporation (ban/kick/other type of removal).
 */
public class AnimLightEvaporation extends BanAnimation {

    /**
     * The length of this ban animation.
     * 40 ticks = 40/20=2 seconds (with 1 second consisting of 20 ticks).
     */
    private static final int DURATION_TICKS = 40;

    public AnimLightEvaporation() {
        super(AnimationType.LIGHT_EVAPORATION);
    }

    @Override
    protected boolean play0(final Player p) {
        // We will play the animation at this location.
        // Moreover, supposed the name of this variable
        // has some logic, we will keep the animation
        // target at it by doing teleports.
        final Location keepAt = p.getLocation().clone();

        keepAt.setYaw(0);
        keepAt.setPitch(0);

        //FIXME THREAD UNSAFE: Potions.clearAll(p);
        //FIXME THREAD UNSAFE: Potions.paralyse(p);

        final TimerTask task;

        new Timer().schedule(task = new ManageableTimerTask() {
            private int duration = DURATION_TICKS;

            @Override
            public void run() {
                // This means that this task has already ran for DURATION_TICKS times.
                if (duration < 0) {
                    this.cancel(); // Terminate and unregister
                    return;
                }

                p.teleport(keepAt);

                // Avoid possible deadlock caused by an error in the animation code
                duration -= 1;

                // The animation itself
                for (double d = 1.5; d >= 0; d -= 0.25) {
                    final double r = d/3;

                    // Magic, @see school.books.BasicGeometry
                    final double x = r * Math.cos(2.5 * d);
                    final double y = 2.5 - d;
                    final double z = r * Math.sin(2.5 * d);

                    // The longer the animation is played - the more particles we generate
                    final int particles = (int) Math.floor(5.0D + ((DURATION_TICKS - duration) / 1.5D));

                    for (int i = 0; i < particles; i++)
                        keepAt.getWorld().playEffect(new Location(keepAt.getWorld(),
                                keepAt.getX() + x, keepAt.getY() + y,
                                keepAt.getZ() + z), Effect.CLOUD, 1);
                }
            }
        }, 50, 50);

        // Lock current thread until the animation has finished playing.
        // This is needed to prevent subsequent actions (if there are
        // any) like RUN_COMMAND from dispatching before the animation
        // is fully done (so that we don't ban before animation ends).
        // ...
        // noinspection StatementWithEmptyBody
        while (TaskManager.isRunning(task));

        // If none of the subsequent commands are removing the player
        // from the server, they will have this slowness effect till
        // their death. But as it is usually difficult to die when you
        // can't move, we'll help them by taking this effect off.
        //FIXME THREAD UNSAFE: p.removePotionEffect(PotionEffectType.SLOW);

        return true;
    }

}
