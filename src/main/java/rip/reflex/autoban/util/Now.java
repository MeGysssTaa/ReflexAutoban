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

package rip.reflex.autoban.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An utility to enable simple access to the current date and time.
 */
public class Now {

    private static final DateFormat df = new SimpleDateFormat("dd.MM.YYYY");
    private static final DateFormat tf = new SimpleDateFormat("HH:mm:ss");

    /**
     * Obtain current date.
     * @return the current date string in dd.MM.YYYY format.
     */
    public static String date() {
        return df.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Obtain current time.
     * @return the current time string in HH:mm:ss format.
     */
    public static String time() {
        return tf.format(new Date(System.currentTimeMillis()));
    }

}
