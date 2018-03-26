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

public class Op {

    /**
     * Player ban animation of the given type at the given player.
     */
    public static final String PLAY_BAN_ANIMATION = "play_ban_animation";

    /**
     * Run the specified command. The command is a string made of all arguments joined together.
     */
    public static final String RUN_COMMAND        = "run_command";

    /**
     * Wait, sleep, do nothing, pass for the specified number of seconds.
     */
    public static final String WAIT               = "wait";

    /**
     * Force all the commands to be ran in the primary thread.
     * MUST BE FIRST IN THE ACTIONS LIST!
     */
    public static final String ALL_SYNC           = "all_sync";

    /**
     * Force all the commands to be ran off the primary thread.
     * MUST BE FIRST IN THE ACTIONS LIST!
     */
    public static final String ALL_ASYNC          = "all_async";

}
