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
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.action.AbstractAction;
import rip.reflex.autoban.util.action.Op;

/**
 * Wait (do nothing and pause further actions execution)
 * the specified amount of seconds.
 */
public class ActionWait extends AbstractAction {

    public ActionWait() {
        super(Op.WAIT, 1);
    }

    @Override
    protected boolean run0(final String[] args) throws Exception {
        final String sStr = args[0];
        final int seconds;

        try {
            seconds = Integer.parseInt(sStr);
        } catch (final NumberFormatException ex) {
            ReflexAutoban.getInstance().getLog().warn("WAIT expected INTEGER at 0, but got '" + sStr + "'");
            Misc.printStackTrace(ex);

            return false;
        }

        Thread.sleep(seconds * 1000);

        return true;
    }

}
