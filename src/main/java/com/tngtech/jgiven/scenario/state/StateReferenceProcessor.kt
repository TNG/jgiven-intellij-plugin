package com.tngtech.jgiven.scenario.state

import com.intellij.psi.PsiField
import com.intellij.psi.PsiReference
import com.intellij.util.Processor
import java.util.*

class StateReferenceProcessor(private val fieldToSearch: PsiField, private val maxNumberOfResults: Int, private val usageProvider: JGivenUsageProvider) : Processor<PsiField> {
    private val results = ArrayList<PsiReference>()

    override fun process(field: PsiField): Boolean {
        val reference = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)
        if (reference != null) {
            results.add(reference)
        }
        return results.size < maxNumberOfResults || maxNumberOfResults == ScenarioStateReferenceProvider.ANY_NUMBER_OF_RESULTS
    }

    fun getResults(): List<PsiReference> = results
}
