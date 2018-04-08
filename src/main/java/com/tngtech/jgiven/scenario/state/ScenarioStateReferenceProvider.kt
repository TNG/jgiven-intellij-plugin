package com.tngtech.jgiven.scenario.state

import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiField
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.util.Processor
import com.tngtech.jgiven.Annotations
import com.tngtech.jgiven.resolution.ResolutionHandler

class ScenarioStateReferenceProvider {
    private val scenarioStateProvider = ScenarioStateAnnotationProvider()
    private val resolutionHandler = ResolutionHandler()

    @JvmOverloads
    fun findReferences(field: PsiField, maxNumberOfResults: Int = ANY_NUMBER_OF_RESULTS): List<PsiReference> {
        val project = field.project
        val manager = PsiManager.getInstance(project)
        val usageProvider = JGivenUsageProvider(scenarioStateProvider, resolutionHandler, ReferenceFactory(manager))
        val processor = StateReferenceProcessor(field, maxNumberOfResults, usageProvider)

        val scope = GlobalSearchScope.everythingScope(project).intersectWith(javaFilesScope(project))

        findPsiFields(project, scope, processor)
        return processor.getResults()
    }

    private fun javaFilesScope(project: Project): GlobalSearchScope {
        return GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), StdFileTypes.JAVA)
    }

    private fun findPsiFields(project: Project, scope: GlobalSearchScope, processor: Processor<PsiField>) {
        Annotations.getScenarioStateClasses(project)
                .forEach { AnnotatedElementsSearch.searchPsiFields(it, scope).forEach(processor) }
    }

    companion object {
        const val ANY_NUMBER_OF_RESULTS = -1
    }
}
