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

package rip.reflex.autoban.util.action;

public class ActionState {

    /**
     * The Action is ready to be executed and is waiting to
     */
    public static final int WAITING = 0;

    /**
     * The Action has started, but not yet stopped executing
     */
    public static final int RUNNING = 1;

    /**
     * The Action has started and successfully ended executing
     */
    public static final int DONE    = 2;

    /**
     * The Action has started executing, but ended with an error
     */
    public static final int ERROR   = 3;

}
