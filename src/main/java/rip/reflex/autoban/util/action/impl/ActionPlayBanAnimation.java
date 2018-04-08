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

package rip.reflex.autoban.util.action.impl;

import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.bananimation.BanAnimations;
import rip.reflex.autoban.util.Misc;
import rip.reflex.autoban.util.action.AbstractAction;
import rip.reflex.autoban.util.action.Op;

/**
 * Play ban animation of the specified type at the given player.
 */
public class ActionPlayBanAnimation extends AbstractAction {

    public ActionPlayBanAnimation() {
        super(Op.PLAY_BAN_ANIMATION, 2);
    }

    @Override
    protected boolean run0(final String[] args) throws Exception {
        final String typeStr = args[0];
        final int type;

        try {
            type = Integer.parseInt(typeStr);
        } catch (final NumberFormatException ex) {
            ReflexAutoban.getInstance().getLog().warn("Action PLAY_BAN_ANIMATION expected INTEGER at 0, but got '" + typeStr + "'");
            Misc.printStackTrace(ex);

            return false;
        }

        return BanAnimations.run(type, Misc.findPlayer(args[1]));
    }

}
