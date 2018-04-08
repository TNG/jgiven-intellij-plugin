package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiTypesUtil

class TypeIsTooGenericCalculator {
    fun typeIsTooGeneric(type: PsiType): Boolean {
        val clazz = PsiTypesUtil.getPsiClass(type) ?: return true
        val qualifiedName = clazz.qualifiedName
        return (qualifiedName == null
                || qualifiedName.startsWith("java.lang")
                || qualifiedName.startsWith("java.io")
                || qualifiedName.startsWith("java.util"))
    }
}
