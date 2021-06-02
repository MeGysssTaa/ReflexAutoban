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

package rip.reflex.autoban.util.misc;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Potion effect related utilities.
 */
public class Potions {

    /**
     * Infinite potion effect duration
     */
    private static final int INFINITY = 1_000_000;

    /**
     * Remove all the active potion effects from the given player.
     */
    public static void clearAll(final Player p) {
        p.getActivePotionEffects().forEach(pe -> p.removePotionEffect(pe.getType()));
    }

    /**
     * Apply extreme slowness potion effect at the given player.
     */
    public static void paralyse(final Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, INFINITY, false, false));
    }

}
