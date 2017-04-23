package com.tngtech.jgiven.util;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class AnnotationProvider {
    public PsiAnnotation findAnnotation(@Nullable PsiModifierListOwner listOwner, @NotNull Collection<String> annotationNames) {
        return AnnotationUtil.findAnnotation(listOwner, annotationNames, false);
    }
}
