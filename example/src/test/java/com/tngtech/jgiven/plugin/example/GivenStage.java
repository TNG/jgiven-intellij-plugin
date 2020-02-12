package com.tngtech.jgiven.plugin.example;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.plugin.example.model.SomeModel;

public class GivenStage extends Stage<GivenStage> {
    @ScenarioState
    SomeModel model;

    public GivenStage an_egg() {
        return this;
    }

    public GivenStage some_milk() {
        return this;
    }

    public GivenStage the_ingredient(String flour) {
        return this;
    }
}
