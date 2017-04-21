package a;

import com.tngtech.jgiven.annotation.ScenarioState;
import a.StateA;

class ForNameReference {
    @ScenarioState(resolution = ScenarioState.Resolution.NAME)
    private StateA stateB;
}