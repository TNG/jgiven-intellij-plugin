package a;

import com.tngtech.jgiven.annotation.ScenarioState;
import a.StateA;

class ForName {
    @ScenarioState(resolution = ScenarioState.Resolution.NAME)
    private StateA stateB<caret>;
}