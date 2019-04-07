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

package rip.reflex.autoban.util.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import rip.reflex.api.Cheat;
import rip.reflex.api.CheckResult;
import rip.reflex.api.ReflexAPI;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.time.Timer;

import java.util.ArrayList;
import java.util.List;
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
     * Should we ignore any violations or PKs this player might get?
     */
    @Getter @Setter
    private boolean ignoring;

    /**
     * Holds the last known confidence of the Reflex's machine learning
     * check that this player is cheating (in percents, 0-100).
     */
    @Getter @Setter
    private double lastMLConf;

    /**
     * Map of logs to cheats.
     */
    private Map<Cheat, List<String>> logs = new ConcurrentHashMap<>();

    /**
     * Tracks time from the last potential kick.
     */
    @Getter
    private final Timer pkTracker = new Timer(0);

    /**
     * Add this player one more potential kick(s).
     * @return the new amount of potential kicks.
     */
    public int addPK(final int number) {
        prePkc = pkc;
        return pkc += number;
    }

    /**
     * Subtract this player one potential kick if possible.
     * Safe to use without checking - PKC will never go below 0.
     */
    public void subtractPK() {
        pkc = Math.max(0, pkc-1);
    }

    /**
     * Reset the amount of potential kicks this player has.
     */
    public Stats resetPKC() {
        prePkc = pkc = 0;
        return this;
    }

    /**
     * Add the given log entry to the list of log entries of the given Cheat
     * as a fancy string similar to Reflex verbose.
     *
     * @param cheat The cheat log entries of which should be appended.
     * @param log The log entry to append by.
     */
    public Stats log(final Cheat cheat, final CheckResult log) {
        final ReflexAPI r = ReflexAutoban.getInstance().reflex();
        final StringBuilder str = new StringBuilder();

        str.append(r.getViolations(player, cheat));
        str.append(" (+").append(log.getViolationsMod()).append(')');

        log.getTags().forEach(tag -> str.append(" | ").append(tag));

        str.append(" | ");
        str.append("tps: ").append(r.getTps()).append(" | ");
        str.append("ping: ").append(r.getPing(player)).append(" | ");
        str.append("lp: ").append(r.getLagPoints(player)).
                append(" (").append(r.getLagStatus(player)).append(")");
        getLogs(cheat).add(str.toString());

        return this;
    }

    /**
     * Returns log entries for the given Cheat.
     * If the logs map doesn't contain the given
     * Cheat yet, an empty ArrayList will be created,
     * put and returned.
     *
     * @param c The cheat to obtain logs of.
     * @return List<CheckResult> - list of log entries the player has got
     *                             on the given cheat so far.
     */
    public List<String> getLogs(final Cheat c) {
        return logs.computeIfAbsent(c, k -> new ArrayList<>());
    }

    /**
     * Check if this player has scored any logs on the given Cheat.
     *
     * @param c The Cheat to look for logs on.
     * @return true if this player has scored 1 or more logs on the given
     *         Cheat so far, false otherwise.
     */
    public boolean hasLogs(final Cheat c) {
        final List<String> logs4c = logs.get(c);
        return logs4c != null && !logs4c.isEmpty();
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
        return map.computeIfAbsent(p, k -> new Stats(p));
    }

    /**
     * Forget everything about the given player.
     * Used to clean memory taken by offline users
     */
    public static void reset(final Player p) {
        map.remove(p);
    }

}
