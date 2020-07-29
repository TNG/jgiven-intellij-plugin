package com.tngtech.jgiven.line.marker

import com.google.common.collect.Iterables
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.impl.LineMarkerNavigator
import com.intellij.codeInsight.daemon.impl.MarkerType
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiIdentifier
import com.tngtech.jgiven.Icons
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider
import com.tngtech.jgiven.util.findParentOfTypeOn
import java.awt.event.MouseEvent

class LineMarkerProvider : com.intellij.codeInsight.daemon.LineMarkerProvider {
    private val scenarioStateReferenceProvider = ScenarioStateReferenceProvider()

    override fun getLineMarkerInfo(element: PsiElement) = collectMarkerFor(element)

    private fun collectMarkerFor(element: PsiElement): JGivenLineMarkerInfo? {
        val field = findParentOfTypeOn<PsiField>(element) ?: return null
        if (element !is PsiIdentifier || element.getParent() !is PsiField) {
            return null
        }
        val references = allReferencingFields(field)
        return when {
            references.isEmpty() -> null
            else -> JGivenLineMarkerInfo(element, Icons.JGIVEN, MarkerType("jgiven", { "JGiven States" }, navigatorToElements()), "JGiven States")
        }
    }

    private fun navigatorToElements(): LineMarkerNavigator {
        return object : LineMarkerNavigator() {

            override fun browse(e: MouseEvent, element: PsiElement) {
                val field = findParentOfTypeOn<PsiField>(element) ?: return
                val references = allReferencingFields(field)
                PsiElementListNavigator.openTargets(e, Iterables.toArray(references, PsiField::class.java),
                        "JGiven States", "", DefaultPsiElementCellRenderer())
            }
        }
    }

    private fun allReferencingFields(element: PsiField) =
            scenarioStateReferenceProvider.findReferences(element, 20)
                    .mapNotNull { findParentOfTypeOn<PsiField>(it.element) }
                    .toList()

}
