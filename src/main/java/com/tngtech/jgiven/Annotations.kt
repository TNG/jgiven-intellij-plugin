package com.tngtech.jgiven

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import java.util.*

object Annotations {
    private const val EXPECTED_SCENARIO_STATE = "com.tngtech.jgiven.annotation.ExpectedScenarioState"
    private const val PROVIDED_SCENARIO_STATE = "com.tngtech.jgiven.annotation.ProvidedScenarioState"
    private const val SCENARIO_STATE = "com.tngtech.jgiven.annotation.ScenarioState"

    val JGIVEN_SCENARIO_STATE_CLASS_NAMES: Collection<String> = Arrays.asList(
            EXPECTED_SCENARIO_STATE, PROVIDED_SCENARIO_STATE, SCENARIO_STATE
    )
    val PROVIDED_SCENARIO_STATE_CLASS_NAMES: Collection<String> = Arrays.asList(
            PROVIDED_SCENARIO_STATE, SCENARIO_STATE
    )
    val EXPECTED_SCENARIO_STATE_CLASS_NAMES: Collection<String> = Arrays.asList(
            EXPECTED_SCENARIO_STATE, SCENARIO_STATE
    )

    private fun findPsiClass(fqClassname: String, javaPsiFacade: JavaPsiFacade, project: Project) =
            javaPsiFacade.findClass(fqClassname, GlobalSearchScope.allScope(project))

    fun getScenarioStateClasses(project: Project) =
            getClassesFor(project, JGIVEN_SCENARIO_STATE_CLASS_NAMES)

    private fun getClassesFor(project: Project, classNames: Collection<String>): Collection<PsiClass> =
            classNames
                    .mapNotNull { findPsiClass(it, JavaPsiFacade.getInstance(project), project) }
                    .toSet()
}
