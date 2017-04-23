package a;

import com.tngtech.jgiven.annotation.ScenarioState;

class ForClassTypeReference {
    @ScenarioState
    private a.StateA state;

    @ScenarioState
    private String myOtherString;
}