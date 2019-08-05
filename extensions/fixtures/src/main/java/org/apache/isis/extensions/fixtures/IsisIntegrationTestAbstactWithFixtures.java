/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.extensions.fixtures;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.extensions.fixtures.api.PersonaWithBuilderScript;
import org.apache.isis.extensions.fixtures.fixturescripts.BuilderScriptAbstract;
import org.apache.isis.extensions.fixtures.fixturescripts.FixtureScript;
import org.apache.isis.extensions.fixtures.fixturescripts.FixtureScripts;
import org.apache.isis.extensions.fixtures.legacy.FixtureClock;
import org.apache.isis.extensions.fixtures.legacy.TickingFixtureClock;
import org.apache.isis.integtestsupport.IsisIntegrationTestAbstact;

public abstract class IsisIntegrationTestAbstactWithFixtures extends IsisIntegrationTestAbstact {

    protected void runFixtureScript(final FixtureScript... fixtureScriptList) {
        this.fixtureScripts.run(fixtureScriptList);
    }

    protected <T> T runBuilderScript(final BuilderScriptAbstract<T> fixtureScript) {
        return this.fixtureScripts.runBuilderScript(fixtureScript);
    }

    protected <T> T runBuilderScript(final PersonaWithBuilderScript<BuilderScriptAbstract<T>> persona) {
        final BuilderScriptAbstract<T> builderScript = persona.builder();
        return runBuilderScript(builderScript);
    }

    /**
     * To use instead of {@link #getFixtureClock()}'s {@link FixtureClock#setDate(int, int, int)} ()}.
     */
    protected void setFixtureClockDate(final LocalDate date) {
        if(date == null) {
            return;
        }
        setFixtureClockDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }

    /**
     * To use instead of {@link #getFixtureClock()}'s {@link FixtureClock#setDate(int, int, int)} ()}.
     */
    protected void setFixtureClockDate(final int year, final int month, final int day) {
        final Clock instance = Clock.getInstance();

        if(instance instanceof TickingFixtureClock) {
            TickingFixtureClock.reinstateExisting();
            getFixtureClock().setDate(year, month, day);
            TickingFixtureClock.replaceExisting();
        }

        if(instance instanceof FixtureClock) {
            getFixtureClock().setDate(year, month, day);
        }
    }

    /**
     * If just require the current time, use {@link ClockService}
     */
    private FixtureClock getFixtureClock() {
        return ((FixtureClock)FixtureClock.getInstance());
    }

    // -- DEPENDENCIES

    @Inject protected FixtureScripts fixtureScripts;


}
