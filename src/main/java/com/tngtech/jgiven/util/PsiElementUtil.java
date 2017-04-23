package com.tngtech.jgiven.util;

import com.intellij.psi.PsiElement;

import java.util.Optional;

public class PsiElementUtil {

    @SuppressWarnings("unchecked")
    public static <E extends PsiElement> Optional<E> findParentOfTypeOn(PsiElement element, Class<E> toFind) {
        if (element == null) {
            return Optional.empty();
        }
        if (toFind.isAssignableFrom(element.getClass())) {
            return Optional.of((E) element);
        }
        return findParentOfTypeOn(element.getParent(), toFind);
    }
}
