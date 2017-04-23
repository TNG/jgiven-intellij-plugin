package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;

class TypeIsTooGenericCalculator {
    boolean typeIsTooGeneric(PsiType type) {
        PsiClass clazz = PsiTypesUtil.getPsiClass(type);
        if (clazz == null) {
            return true;
        }
        String qualifiedName = clazz.getQualifiedName();
        return qualifiedName == null
                || qualifiedName.startsWith("java.lang")
                || qualifiedName.startsWith("java.io")
                || qualifiedName.startsWith("java.util");
    }
}
