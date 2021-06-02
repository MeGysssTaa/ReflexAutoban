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

package rip.reflex.autoban.banwave;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.str.Strings;
import rip.reflex.autoban.util.placeholders.PlaceholderApplicator;

import java.util.HashSet;
import java.util.Set;

/**
 * ReflexAutoban ban wave implementation.
 * Stores and controls usernames of players
 * who have been queued to be banned.
 */
public class BanWave {

    /**
     * Set of player names to ban.
     * We store names rather than Player objects because players
     * can quit or rejoin the server to avoid being banned.
     */
    @Getter
    private final Set<String> players = new HashSet<>();

    /**
     * Do not run this ban wave on schedule.
     * Used for forced runs, e.g. with a command.
     */
    @Getter @Setter
    private boolean skip;

    /**
     * Approximately calculated time of the next regular ban wave schedule.
     */
    @Getter
    private long nextSchedule;

    /**
     * Add the name of the given player to this ban wave.
     * @param p The player whose name should be added.
     */
    public BanWave addPlayer(final Player p) {
        return addPlayer(p.getName().toLowerCase());
    }

    /**
     * Add the given name to this ban wave
     * @param name The name to add
     */
    public BanWave addPlayer(final String name) {
        if (!(players.contains(name)))
            players.add(name);
        return this;
    }

    /**
     * Remove the name of the given player from this ban wave.
     * @param p The player whose name should be removed.
     */
    public BanWave removePlayer(final Player p) {
        return removePlayer(p.getName().toLowerCase());
    }

    /**
     * Remove the given name from this ban wave.
     * @param name The name to remove.
     */
    public BanWave removePlayer(final String name) {
        players.remove(name);
        return this;
    }

    /**
     * Clear the ban queue. Forever and fully.
     */
    public BanWave clear() {
        players.clear();
        return this;
    }

    /**
     * Returns the number of players in this ban wave.
     * @return the number of players in this ban wave.
     */
    public int size() {
        return players.size();
    }

    /**
     * Run this ban wave, executing actions at all the players in it.
     *
     * @param schedule Should be true if this ban wave is scheduled automatically, false if forcefully.
     * @return the number of players banned in this ban wave.
     */
    public int run(final boolean schedule) {
        if (schedule) {
            // current_time + period
            nextSchedule = System.currentTimeMillis()
                    + (ReflexAutoban.getInstance().getSetting("ban_wave_period", 15) * 60 * 1000);
            boolean shallSkip = skip;
            skip = false; // Don't skip next ban wave

            if (shallSkip) { // Skip this one
                ReflexAutoban.getInstance().getLog().info("Skipped this regular ban wave");
                return 0;
            }
        } else skip = true; // Skip next ban wave

        final int numP = players.size();

        // Execute actions.ban_wave at all players in the ban wave
        for (final String name : players) {
            try {
                // Find this skid whereever he is hiding
                Player p = Misc.findPlayer(name);

                if (p == null) // Someone is too good in HAS...
                    p = new MissingPlayer(name);
                Misc.execute(ReflexAutoban.getInstance().getConfig().getStringList("actions.ban_wave"), p,
                        "%id%", Strings.generateBanID()
                );
            } catch (final Exception ex) {
                ReflexAutoban.getInstance().getLog().warn("Failed to execute banwave commands at '" + name + "'");
                Misc.printStackTrace(ex);
            }
        }

        // We don't want all the subsequent ban waves to run
        // actions at same players again and again...
        players.clear();

        if (numP != 0)
            // Broadcast the ban_wave_end message to all online players if there was at least 1 player banned
            for (final Player p : Bukkit.getOnlinePlayers())
                p.sendMessage(Strings.color(PlaceholderApplicator.apply(ReflexAutoban.getInstance().getLang("ban_wave_end"),
                p, ReflexAutoban.getInstance().reflex(), "%players_banned%", Integer.toString(numP))));
        return numP;
    }

}
