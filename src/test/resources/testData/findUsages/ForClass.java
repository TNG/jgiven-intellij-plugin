package a;

import com.tngtech.jgiven.annotation.ScenarioState;
import a.StateA;

class ForClass {
    @ScenarioState
    private StateA stateA<caret>;
}