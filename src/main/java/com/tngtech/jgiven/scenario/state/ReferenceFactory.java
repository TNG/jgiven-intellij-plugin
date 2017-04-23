package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMemberReference;

public class ReferenceFactory {
    private final PsiManager manager;

    public ReferenceFactory(PsiManager manager) {
        this.manager = manager;
    }

    PsiReference referenceFor(PsiField field) {
        return new LightMemberReference(manager, field, PsiSubstitutor.EMPTY) {
            @Override
            public PsiElement getElement() {
                return field;
            }
        };
    }

}
