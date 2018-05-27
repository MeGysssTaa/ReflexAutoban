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

package rip.reflex.autoban.bananimation;

import org.bukkit.entity.Player;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.bananimation.impl.AnimLightEvaporation;
import rip.reflex.autoban.util.misc.Misc;

import java.util.HashSet;
import java.util.Set;

/**
 * BanAnimations-related utilities.
 */
public class BanAnimations {

    /**
     * List of registered ban animations
     */
    private static final Set<BanAnimation> banAnimations = new HashSet<>();

    /**
     * Initialize the ban animations
     */
    public static void init() {
        register(new AnimLightEvaporation());
    }

    /**
     * Add the given ban animation to the defined ban animations set.
     * @param a The ban animation to add.
     */
    public static void register(final BanAnimation a) {
        banAnimations.add(a);
    }

    /**
     * Find ban animation by the given type and
     * play it at the given player.
     *
     * @param type The type to search by
     * @param target The player to play ban animation at
     *
     * @see AnimationType
     * @return true if ban animation was played successfully, false in case of failure
     */
    public static boolean run(final int type, final Player target) {
        try {
            final BanAnimation anim;

            if ((anim = banAnimations.stream().filter(a -> a.getId() == type).findFirst().orElse(null)) != null)
                return anim.play(target);

            ReflexAutoban.getInstance().getLog().warn("Undefined ban animation '" + type + "'");
            return false;
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn(String.format("Error playing ban animation '%s' at player %s. Make sure to check your syntax!", type, (target == null) ? "<NULL>" : target.getName()));
            Misc.printStackTrace(ex);

            return false;
        }
    }

}
