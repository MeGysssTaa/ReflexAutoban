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

package rip.reflex.autoban.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An object that is used to store information about
 * players like their PK count and other stuff.
 */
@RequiredArgsConstructor
public class Stats {

    @Getter
    private final Player player;

    /**
     * How many times has the player been potentially kicked?
     */
    @Getter
    private int pkc, prePkc;

    /**
     * Add player one more potential kick.
     * @return the new amount of potential kicks.
     */
    public int addPK() {
        prePkc = pkc;
        return pkc += 1;
    }

    /**
     * Reset the amount of potential kicks this player has.
     */
    public Stats resetPKC() {
        prePkc = pkc = 0;
        return this;
    }

    /**
     * Using the thread-safe ConcurrentHashMap to avoid
     * rare server crashes w/ multi-thread access.
     */
    private static final Map<Player, Stats> map = new ConcurrentHashMap<>();

    /**
     * Obtain or create the Stats object of the given player.
     *
     * @param p The player to obtain stats of, or to create ones for.
     * @return The Stats object corresponding to the given player.
     */
    public static Stats of(final Player p) {
        Stats s = map.get(p);

        if (s == null)
            map.put(p, s = new Stats(p));
        return s;
    }

    /**
     * Forget everything about the given player.
     * Used to clean memory taken by offline users
     */
    public static void reset(final Player p) {
        map.remove(p);
    }

}
