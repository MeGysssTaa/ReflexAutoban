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

package rip.reflex.autoban;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.reflex.api.Cheat;
import rip.reflex.api.CheckResult;
import rip.reflex.api.ReflexAPIProvider;
import rip.reflex.api.event.ReflexCheckEvent;
import rip.reflex.api.event.ReflexCommandEvent;
import rip.reflex.api.event.ReflexLoadEvent;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.misc.Stats;
import rip.reflex.autoban.util.str.Strings;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    /**
     * For easier access (instead of ReflexAutoban#getInstance)
     */
    private final ReflexAutoban inst;

    EventListener(final ReflexAutoban instance) {
        // Register the listener assigning the instance
        Bukkit.getPluginManager().registerEvents(this, this.inst = instance);
    }

    /**
     * Reflex notifies other plugins of it's complete startup and readiness
     * to work with this event. We listen to it to obtain the API instance
     */
    @EventHandler
    public void reflexLoaded(final ReflexLoadEvent e) {
        inst.setReflex(ReflexAPIProvider.getAPI());
        inst.finishInit();
    }

    /**
     * Called whenever Reflex checks anyone.
     * We handle this method to check whether the check
     * result is "failed" (which means Reflex is sure the
     * player is cheating) to store logs for potential
     * cheaters.
     */
    @EventHandler
    public void playerChecked(final ReflexCheckEvent e) {
        // In case there is another plugin with higher priority API hook cancelling some Reflex events,
        // if Reflex API is not loaded/ready-to-use yet, or if the check is passed (the player is legit).
        if ((e.isCancelled()) || (!(inst.isReflexReady())) || (e.getResult().isCheckPassed()))
            return;

        final Stats stats = Stats.of(e.getPlayer());

        final CheckResult res = e.getResult();
        final Cheat cheat = e.getCheat();

        if (stats.isIgnoring())
            return;

        // Log this violation
        stats.log(cheat, res);

        if (cheat == Cheat.KillAura) {
            // Find out if this was the machine learning check detection, and,
            // if so, set this check's cheating confidence as the last known
            // machine learning detection confidence for this player.
            res.getTags().forEach(tag -> {
                if (tag.startsWith("conviction"))
                    // Found. Get the percent part of a String like "conviction: 95.25%"
                    stats.setLastMLConf(Double.valueOf(Strings.replace(tag.split(" ")[1], "%", "")));
            });
        }
    }

    /**
     * Called whenever Reflex executes a command against a potential cheater
     * from its configuration file. In case of default config this means that
     * this event gets fired when Reflex wants to kick a player. As ReflexAutoban
     * is meant to work with default actions lists, we listen for such events,
     * terminate them so that Reflex doesn't kick anyone, and do things we need.
     */
    @EventHandler
    public void playerPotantiallyKicked(final ReflexCommandEvent e) {
        // In case there is another plugin with higher priority API hook cancelling some Reflex events
        if (e.isCancelled())
            return;

        e.setCancelled(true); // Don't let Reflex do what it wants

        // Make sure that the command is somehow related to kicking and the Reflex API is ready
        if ((!(inst.isReflexReady())) || (!(e.getCommand().contains("kick"))))
            return;

        final Player p = e.getPlayer();
        final Stats stats = Stats.of(p);

        if (stats.isIgnoring())
            return;

        int weight = Math.max(0, inst.getSetting("weights." + e.getCheat().name().toLowerCase(), 0));
        int doubleRate = inst.getSetting("double_weight_rate", 45) /* seconds */ * 1000 /* milliseconds */;

        double doubleConf = inst.getSetting("double_weight_confidence", 80);

        if ((!(stats.getPkTracker().hasPassed(doubleRate))) || (stats.getLastMLConf() > doubleConf))
            weight *= 2;

        // Register this PK
        final int curPKc = stats.addPK(weight);
        final int prePKc = stats.getPrePkc();

        stats.getPkTracker().reset();
        inst.reflex().reset(p);

        // Loop through all the actions to find ones matching the 'P < C >= G' condition,
        // where 'P' is player's previous PKC, 'C' - current, and 'G' is the amount of PKs
        // stated at the given entry in config, and add them in this 'actions' list.
        final List<String> actions = new ArrayList<>();

        inst.getConfig().getConfigurationSection("actions").getKeys(false).stream().filter(vl -> {
            if (vl.equals("ban_wave"))
                return false;

            final int pki = Integer.parseInt(vl);
            return prePKc < pki && curPKc >= pki;
        }).forEach(s -> actions.addAll(inst.getConfig().getStringList("actions." + s)));

        // Execute the actions
        Misc.execute(actions, p,
                "%pkc%", Integer.toString(curPKc),
                "%prev_pkc%", Integer.toString(prePKc),
                "%id%", e.getViolationId()
        );
    }

    /**
     * Free memory by removing entries which will no longer be used.
     * In this case, we're removing players which quit the server
     */
    @EventHandler
    public void playerQuit(final PlayerQuitEvent e) {
        Stats.reset(e.getPlayer());
    }

}
