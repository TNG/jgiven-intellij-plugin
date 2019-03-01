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
import com.intellij.psi.PsiReference
import com.tngtech.jgiven.Icons
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider
import com.tngtech.jgiven.util.PsiElementUtil
import java.awt.event.MouseEvent

class LineMarkerProvider : com.intellij.codeInsight.daemon.LineMarkerProvider {
    private val scenarioStateReferenceProvider = ScenarioStateReferenceProvider()

    override fun getLineMarkerInfo(element: PsiElement) = collectMarkerFor(element)

    private fun collectMarkerFor(element: PsiElement): JGivenLineMarkerInfo? {
        val field = PsiElementUtil.findParentOfTypeOn(element, PsiField::class.java)
        if (element !is PsiIdentifier || element.getParent() !is PsiField) {
            return null
        }
        val references = allReferencingFields(field.orElseThrow<IllegalArgumentException> { IllegalArgumentException() })
        return when {
            references.isEmpty() -> null
            else -> JGivenLineMarkerInfo(element, Icons.JGIVEN, MarkerType("jgiven", { _ -> "JGiven States" }, navigatorToElements()), "JGiven States")
        }

    }

    private fun navigatorToElements(): LineMarkerNavigator {
        return object : LineMarkerNavigator() {

            override fun browse(e: MouseEvent, element: PsiElement) {
                val field = PsiElementUtil.findParentOfTypeOn(element, PsiField::class.java)
                val references = allReferencingFields(field.orElseThrow<IllegalArgumentException> { IllegalArgumentException() })
                PsiElementListNavigator.openTargets(e, Iterables.toArray(references, PsiField::class.java),
                        "JGiven States", "", DefaultPsiElementCellRenderer())
            }
        }
    }

    override fun collectSlowLineMarkers(elements: List<PsiElement>, result: Collection<LineMarkerInfo<*>>) {}

    private fun allReferencingFields(element: PsiField) =
            scenarioStateReferenceProvider.findReferences(element, 20)
                    .map { this.fieldOf(it) }
                    .filter { it != null }
                    .map { it!! }
                    .toList()

    private fun fieldOf(r: PsiReference) =
            PsiElementUtil.findParentOfTypeOn(r.element, PsiField::class.java).orElse(null)
}
