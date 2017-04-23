package a;

import com.tngtech.jgiven.annotation.ScenarioState;
import a.StateA;

class ForClassTypeReference {
    @ScenarioState
    private StateA state;

    @ScenarioState
    private String myOtherString;
}