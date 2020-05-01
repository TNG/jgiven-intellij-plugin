package com.tngtech.jgiven.util

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiModifierListOwner

class AnnotationProvider {
    fun findAnnotation(listOwner: PsiModifierListOwner?, annotationNames: Collection<String>) =
            AnnotationUtil.findAnnotation(listOwner, annotationNames, false)
}
