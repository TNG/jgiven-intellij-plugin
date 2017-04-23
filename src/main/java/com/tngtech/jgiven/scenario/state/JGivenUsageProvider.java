package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.tngtech.jgiven.resolution.ResolutionHandler;

import javax.annotation.Nullable;

class JGivenUsageProvider {

    private final ScenarioStateAnnotationProvider scenarioStateProvider;
    private final ResolutionHandler resolutionHandler;
    private final ReferenceFactory referenceFactory;

    JGivenUsageProvider(ScenarioStateAnnotationProvider scenarioStateProvider, ResolutionHandler resolutionHandler, ReferenceFactory referenceFactory) {
        this.scenarioStateProvider = scenarioStateProvider;
        this.resolutionHandler = resolutionHandler;
        this.referenceFactory = referenceFactory;
    }

    @Nullable
    PsiReference createReferenceIfJGivenUsage(PsiField fieldToSearch, PsiField field) {
        if (scenarioStateProvider.isJGivenScenarioState(field)
                && !fieldToSearch.equals(field)
                && resolutionHandler.resolutionMatches(field, fieldToSearch)) {
            return referenceFactory.referenceFor(field);
        }
        return null;
    }
}
