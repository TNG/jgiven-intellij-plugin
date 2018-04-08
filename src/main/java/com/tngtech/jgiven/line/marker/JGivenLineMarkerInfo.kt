package com.tngtech.jgiven.line.marker

import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.MergeableLineMarkerInfo
import com.intellij.codeInsight.daemon.impl.MarkerType
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import javax.swing.Icon

@Suppress("UNCHECKED_CAST")
class JGivenLineMarkerInfo(element: PsiElement, icon: Icon, markerType: MarkerType, private val text: String) : MergeableLineMarkerInfo<PsiElement>(element, element.textRange, icon, Pass.UPDATE_ALL, markerType.tooltip, markerType.navigationHandler, GutterIconRenderer.Alignment.LEFT) {

    override fun canMergeWith(info: MergeableLineMarkerInfo<*>): Boolean {
        if (info !is JGivenLineMarkerInfo) return false
        val otherElement = info.getElement()
        val myElement = element
        return otherElement != null && myElement != null
    }


    override fun getCommonIcon(infos: List<MergeableLineMarkerInfo<*>>): Icon {
        return myIcon
    }

    override fun getCommonTooltip(infos: List<MergeableLineMarkerInfo<*>>): Function<in PsiElement, String> {
        return { _: PsiElement -> text } as (Function<PsiElement, String>)
    }
}