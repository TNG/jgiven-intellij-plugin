package com.tngtech.jgiven.plugin.example;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class DummyTest extends ScenarioTest<GivenStage, WhenStage, ThenStage> {
    @Test
    public void a_pancake_can_be_fried_out_of_an_egg_milk_and_flour() {
        given().an_egg().
                and().some_milk().
                and().the_ingredient( "flour" );

        when().the_cook_mangles_everything_to_a_dough().
                and().the_cook_fries_the_dough_in_a_pan();

        then().the_resulting_meal_is_a_pancake();
    }
}
