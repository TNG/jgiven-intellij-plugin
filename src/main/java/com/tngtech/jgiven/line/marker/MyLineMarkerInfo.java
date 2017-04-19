package com.tngtech.jgiven.line.marker;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.MergeableLineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class MyLineMarkerInfo extends MergeableLineMarkerInfo<PsiElement> {

    private final String text;

    public MyLineMarkerInfo(@NotNull PsiElement element, Icon icon, @NotNull MarkerType markerType, String text) {
        super(element, element.getTextRange(), icon, Pass.UPDATE_ALL, markerType.getTooltip(),
                markerType.getNavigationHandler(), GutterIconRenderer.Alignment.LEFT);
        this.text = text;
    }

    @Override
    public boolean canMergeWith(@NotNull MergeableLineMarkerInfo<?> info) {
        if (!(info instanceof MyLineMarkerInfo)) return false;
        PsiElement otherElement = info.getElement();
        PsiElement myElement = getElement();
        return otherElement != null && myElement != null;
    }


    @Override
    public Icon getCommonIcon(@NotNull List<MergeableLineMarkerInfo> infos) {
        return myIcon;
    }

    @NotNull
    @Override
    public Function<? super PsiElement, String> getCommonTooltip(@NotNull List<MergeableLineMarkerInfo> infos) {
        return element -> text;
    }
}
