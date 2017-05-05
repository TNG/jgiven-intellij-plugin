package com.tngtech.jgiven;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Annotations {
    private static final String EXPECTED_SCENARIO_STATE = "com.tngtech.jgiven.annotation.ExpectedScenarioState";
    private static final String PROVIDED_SCENARIO_STATE = "com.tngtech.jgiven.annotation.ProvidedScenarioState";
    private static final String SCENARIO_STATE = "com.tngtech.jgiven.annotation.ScenarioState";

    public static final Collection<String> JGIVEN_SCENARIO_STATE_ANNOTATION_NAMES = ImmutableSet.of(
            "ExpectedScenarioState",
            "ProvidedScenarioState",
            "ScenarioState");

    public static final Collection<String> JGIVEN_SCENARIO_STATE_CLASS_NAMES = Arrays.asList(
            EXPECTED_SCENARIO_STATE, PROVIDED_SCENARIO_STATE, SCENARIO_STATE
    );
    public static final Collection<String> PROVIDED_SCENARIO_STATE_CLASS_NAMES = Arrays.asList(
            PROVIDED_SCENARIO_STATE, SCENARIO_STATE
    );
    public static final Collection<String> EXPECTED_SCENARIO_STATE_CLASS_NAMES = Arrays.asList(
            EXPECTED_SCENARIO_STATE, SCENARIO_STATE
    );

    private static PsiClass findPsiClass(String fqClassname, JavaPsiFacade javaPsiFacade, Project project) {
        return javaPsiFacade.findClass(fqClassname, GlobalSearchScope.allScope(project));
    }

    public static Collection<PsiClass> getScenarioStateClasses(Project project) {
        return getClassesFor(project, JGIVEN_SCENARIO_STATE_CLASS_NAMES);
    }

    private static Collection<PsiClass> getClassesFor(Project project, Collection<String> classNames) {
        return classNames.stream()
                .map(a -> findPsiClass(a, JavaPsiFacade.getInstance(project), project))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
