package a;

import com.tngtech.jgiven.annotation.ScenarioState;
import a.StateA;

class SomeReference {
    @ScenarioState(resolution = ScenarioState.Resolution.NAME)
    private StateA stateB;

    @ScenarioState
    private String myString;
}