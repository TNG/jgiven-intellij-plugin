package com.tngtech.jgiven;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;

public class Annotations {
    public static final Collection<String> JGIVEN_SCENARIO_STATE_CLASS_NAMES =
            ImmutableSet.of("com.tngtech.jgiven.annotation.ExpectedScenarioState",
                    "com.tngtech.jgiven.annotation.ProvidedScenarioState",
                    "com.tngtech.jgiven.annotation.ScenarioState");

}
