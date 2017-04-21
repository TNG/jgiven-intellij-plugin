package com.tngtech.jgiven;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.stream.Collectors;

public class Annotations {
    public static final Collection<String> JGIVEN_SCENARIO_STATE_ANNOTATION_NAMES = ImmutableSet.of(
            "ExpectedScenarioState",
            "ProvidedScenarioState",
            "ScenarioState");

    public static final Collection<String> JGIVEN_SCENARIO_STATE_CLASS_NAMES =
            JGIVEN_SCENARIO_STATE_ANNOTATION_NAMES.stream()
                    .map(el -> "com.tngtech.jgiven.annotation." + el)
                    .collect(Collectors.toSet());

    public static PsiClass findPsiClass(String fqClassname, JavaPsiFacade javaPsiFacade, Project project) {
        return javaPsiFacade.findClass(fqClassname, GlobalSearchScope.allScope(project));
    }

    public static Collection<PsiClass> getScenarioStateClasses(Project project) {
        return JGIVEN_SCENARIO_STATE_CLASS_NAMES.stream()
                .map(a -> findPsiClass(a, JavaPsiFacade.getInstance(project), project))
                .collect(Collectors.toSet());
    }
}
