package com.tngtech.jgiven.util

import com.intellij.psi.PsiElement

inline fun <reified E : PsiElement> findParentOfTypeOn(element: PsiElement?): E? {
    element ?: return null

    var current: PsiElement? = element
    while(current != null) {
        if (current is E) {
            return current
        }
        current = current.parent
    }
    return null
}
