/*
 * Copyright 2020 DarksideCode
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

package rip.reflex.autoban.util.time;

/**
 * A convenient utility used for time tracking.
 */
public class Timer {

    private long startTime;

    public Timer(final long startTime) {
        this.startTime = startTime;
    }

    public Timer() {
        this(System.currentTimeMillis());
    }

    public Timer add(final long millis) {
        startTime += Math.abs(millis);
        return this;
    }

    public Timer addTicks(final long ticks) {
        startTime += Math.abs(ticks * 50);
        return this;
    }

    public Timer addSeconds(final long seconds) {
        startTime += Math.abs(seconds * 1000);
        return this;
    }

    public Timer subtract(final long millis) {
        startTime -= Math.abs(millis);
        return this;
    }

    public Timer subtractTicks(final long ticks) {
        startTime -= Math.abs(ticks) * 50;
        return this;
    }

    public Timer subtractSeconds(final long seconds) {
        startTime -= Math.abs(seconds * 1000);
        return this;
    }

    public Timer offset(final long millis) {
        startTime += millis;
        return this;
    }

    public Timer offsetTicks(final long ticks) {
        startTime += ticks * 50;
        return this;
    }

    public Timer offsetSeconds(final long seconds) {
        startTime += seconds * 1000;
        return this;
    }

    public boolean hasPassed(final long millis) {
        return getTimePassed() >= millis;
    }

    public boolean hasTicked(final long ticks) {
        return getTimeTicked() >= ticks;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - startTime;
    }

    public long getTimeTicked() {
        return getTimePassed() / 50;
    }

    public long getStartTime() {
        return startTime;
    }

    public Timer setStartTime(final long newTime) {
        startTime = newTime;
        return this;
    }

    public Timer reset() {
        setStartTime(System.currentTimeMillis());
        return this;
    }
    
}
