package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTypesUtil;
import com.tngtech.jgiven.resolution.ResolutionHandler;
import com.tngtech.jgiven.util.PsiElementUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioStateReferenceProvider {

    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
    private ResolutionHandler resolutionHandler = new ResolutionHandler();

    public List<PsiReference> findReferences(PsiField field) {
        PsiClass fieldClass = PsiTypesUtil.getPsiClass(field.getType());
        if (fieldClass == null) {
            return Collections.emptyList();
        }
        return ReferencesSearch.search(fieldClass)
                .findAll().stream()
                .filter(r -> scenarioStateProvider.isJGivenScenarioState(fieldOf(r)))
                .filter(r -> !fieldOf(r).equals(field))
                .filter(r -> resolutionHandler.resolutionMatches(fieldOf(r), field))
                .collect(Collectors.toList());
    }

    private PsiField fieldOf(PsiReference r) {
        return PsiElementUtil.findParentOfTypeOn(r.getElement(), PsiField.class).orElse(null);
    }
}
