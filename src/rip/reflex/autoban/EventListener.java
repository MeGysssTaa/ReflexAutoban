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

package rip.reflex.autoban;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.reflex.api.ReflexAPIProvider;
import rip.reflex.api.event.ReflexCommandEvent;
import rip.reflex.api.event.ReflexLoadEvent;
import rip.reflex.autoban.util.Misc;
import rip.reflex.autoban.util.Stats;
import rip.reflex.autoban.util.UnsupportedAPIException;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    /**
     * For easier access (instead of ReflexAutoban#getInstance)
     */
    private final ReflexAutoban instance;

    EventListener(final ReflexAutoban instance) {
        // Register the listener assigning the instance
        Bukkit.getPluginManager().registerEvents(this, this.instance = instance);
    }

    /**
     * Reflex notifies other plugins of it's complete startup and readiness
     * to work with this event. We listen to it to obtain the API instance
     */
    @EventHandler
    public void reflexLoaded(final ReflexLoadEvent e) {
        // Shutdown forcefully in case if API version of Reflex installed
        // on the server does not match API version we can work with.
        if (e.getApiVersion() != ReflexAutoban.API_VERSION) {
            Bukkit.getPluginManager().disablePlugin(instance);
            throw new UnsupportedAPIException(e.getApiVersion());
        }

        instance.setReflex(ReflexAPIProvider.getAPI());
        instance.finishInit();
    }

    /**
     * Called whenever Reflex executes a command against a potential cheater
     * from its configuration file. In case of default config this means that
     * this event gets fired when Reflex wants to kick a player. As ReflexAutoban
     * is meant to work with default actions lists, we listen for such events,
     * terminate them so that Reflex doesn't kick anyone, and do things we need.
     */
    @EventHandler
    public void cheaterDetected(final ReflexCommandEvent e) {
        e.setCancelled(true); // Don't let Reflex do what it wants

        // Make sure that the command is somehow related to kicking and the Reflex API is ready
        if ((!(instance.isReflexReady())) || (!(e.getCommand().contains("kick"))))
            return;

        final Player p = e.getPlayer();
        final Stats stats = Stats.of(p);

        // Register this PK
        final int curPKc = stats.addPK();
        final int prePKc = stats.getPrePkc();

        instance.reflex().reset(p);

        // Loop through all the actions to find ones matching the 'P < C >= G' condition,
        // where 'P' is player's previous PKC, 'C' - current, and 'G' is the amount of PKs
        // stated at the given entry in config, and add them in this 'actions' list.
        final List<String> actions = new ArrayList<>();

        instance.getConfig().getConfigurationSection("actions").getKeys(false).stream().filter(vl -> {
            if (vl.equals("ban_wave"))
                return false;

            final int pki = Integer.parseInt(vl);
            return prePKc < pki && curPKc >= pki;
        }).forEach(s -> actions.addAll(instance.getConfig().getStringList("actions." + s)));

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
