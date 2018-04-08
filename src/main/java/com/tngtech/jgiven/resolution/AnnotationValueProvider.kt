package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiExpression

class AnnotationValueProvider {

    fun getAnnotationValue(annotation: PsiAnnotation, annotationKey: String): PsiExpression? {
        val attributes = annotation.parameterList.attributes
        return attributes
                .filter { a -> annotationKey.equals(a.name!!, ignoreCase = true) }
                .map { it.value }
                .filter { v -> v is PsiExpression }
                .map { v -> v as PsiExpression }
                .firstOrNull()
    }
}
