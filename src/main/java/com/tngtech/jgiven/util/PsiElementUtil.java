package com.tngtech.jgiven.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierListOwner;

import java.util.Optional;

public class PsiElementUtil {

    public static boolean hasModifier(PsiModifierListOwner element, String modifier) {
        return element.getModifierList() != null && element.getModifierList().hasExplicitModifier(modifier);
    }

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
