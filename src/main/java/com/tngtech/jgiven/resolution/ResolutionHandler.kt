package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiField

class ResolutionHandler(
        private val resolutionProvider: ResolutionProvider = ResolutionProvider()
) {

    fun resolutionMatches(field: PsiField, fieldToSearch: PsiField): Boolean {
        val fieldResolution = resolutionProvider.getResolutionFrom(field)
        val resolution = resolutionProvider.getResolutionFrom(fieldToSearch)
        if (resolution !== fieldResolution) {
            return false
        }
        return if (resolution !== Resolution.NAME) {
            field.type.presentableText.equals(fieldToSearch.type.presentableText, ignoreCase = true)
        } else field.name == fieldToSearch.name
    }
}
