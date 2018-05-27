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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.banwave.MissingPlayer;
import rip.reflex.autoban.util.misc.Misc;

/**
 * The object to extend when creating BanAnimation implementations.
 */
@RequiredArgsConstructor
public abstract class BanAnimation {

    /**
     * Type (identification number) of this ban animation.
     */
    @Getter
    private final int id;

    /**
     * Play this ban animation at the given player.
     * If the given player is null or an instance of
     * MissingPlayer, simply success is returned.
     *
     * @param p The player to play the animation at.
     * @return true if animation was played successfully, false in case of error.
     */
    public final boolean play(final Player p) {
        if ((p == null) || (p instanceof MissingPlayer))
            return true;

        try {
            return play0(p);
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn(String.format("Failed to play ban animation %s at %s", id, p.getName()));
            Misc.printStackTrace(ex);

            return false;
        }
    }

    protected abstract boolean play0(final Player p);

}
