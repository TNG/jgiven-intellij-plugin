package com.tngtech.jgiven.plugin.example;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.plugin.example.model.SomeModel;

public class ThenStage extends Stage<ThenStage> {
    @ScenarioState
    SomeModel model;

    public ThenStage the_resulting_meal_is_a_pancake() {
        return this;
    }
}
