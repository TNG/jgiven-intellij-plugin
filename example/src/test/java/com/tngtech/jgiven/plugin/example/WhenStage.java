package com.tngtech.jgiven.plugin.example;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.plugin.example.model.SomeModel;

public class WhenStage extends Stage<WhenStage> {
    @ScenarioState
    SomeModel model;

    public WhenStage the_cook_mangles_everything_to_a_dough() {
        return this;
    }

    public WhenStage the_cook_fries_the_dough_in_a_pan() {
        return this;
    }
}
