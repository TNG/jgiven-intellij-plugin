package com.tngtech.jgiven.scenario.state

import com.intellij.psi.*
import com.intellij.psi.impl.light.LightMemberReference

class ReferenceFactory(private val manager: PsiManager) {

    fun referenceFor(field: PsiField): PsiReference =
            object : LightMemberReference(manager, field, PsiSubstitutor.EMPTY) {
                override fun getElement(): PsiElement = field
            }
}
