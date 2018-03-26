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
    protected boolean run0(String[] args) throws Exception {

        return true;
    }

}
