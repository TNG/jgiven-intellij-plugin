package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiField
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider

class ResolutionProvider(
        private val scenarioStateProvider: ScenarioStateAnnotationProvider = ScenarioStateAnnotationProvider(),
        private val annotationValueProvider: AnnotationValueProvider = AnnotationValueProvider(),
        private val typeIsTooGeneric: TypeIsTooGenericCalculator = TypeIsTooGenericCalculator()
) {


    fun getResolutionFrom(field: PsiField): Resolution? {
        val annotation = scenarioStateProvider.getJGivenAnnotationOn(field) ?: return null

        val annotationValue = annotationValueProvider.getAnnotationValue(annotation, FIELD_RESOLUTION)

        return annotationValue?.text
                ?.let { value ->
                    Resolution.values()
                            .firstOrNull { it !== Resolution.AUTO && value.contains(it.name) }
                }
                ?: getResolutionForFieldType(field)
    }

    /**
     * Calculate default resolution. See {[com.tngtech.jgiven.impl.inject.ScenarioStateField.getResolution]}
     */
    @Suppress("KDocUnresolvedReference")
    private fun getResolutionForFieldType(field: PsiField) = when {
        typeIsTooGeneric.typeIsTooGeneric(field.type) -> Resolution.NAME
        else -> Resolution.TYPE
    }

    companion object {
        const val FIELD_RESOLUTION = "resolution"
    }
}
