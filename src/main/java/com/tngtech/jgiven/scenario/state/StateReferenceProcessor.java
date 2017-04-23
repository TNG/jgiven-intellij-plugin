package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.util.Processor;

import java.util.ArrayList;
import java.util.List;

class StateReferenceProcessor implements Processor<PsiField> {

    private PsiField fieldToSearch;
    private int maxNumberOfResults;
    private JGivenUsageProvider usageProvider;
    private List<PsiReference> results = new ArrayList<>();

    StateReferenceProcessor(PsiField fieldToSearch, int maxNumberOfResults, JGivenUsageProvider usageProvider) {
        this.fieldToSearch = fieldToSearch;
        this.maxNumberOfResults = maxNumberOfResults;
        this.usageProvider = usageProvider;
    }

    @Override
    public boolean process(PsiField field) {
        PsiReference reference = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field);
        if (reference != null) {
            results.add(reference);
        }
        return results.size() < maxNumberOfResults || maxNumberOfResults == ScenarioStateReferenceProvider.ANY_NUMBER_OF_RESULTS;
    }

    List<PsiReference> getResults() {
        return results;
    }
}
