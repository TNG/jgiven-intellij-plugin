package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiField;

import java.util.Objects;

public class ResolutionHandler {

    private ResolutionProvider resolutionProvider = new ResolutionProvider();

    public boolean resolutionMatches(PsiField field, PsiField fieldToSearch) {
        Resolution fieldResolution = resolutionProvider.getResolutionFrom(field);
        Resolution resolution = resolutionProvider.getResolutionFrom(fieldToSearch);
        if (resolution != fieldResolution) {
            return false;
        }
        if (resolution != Resolution.NAME) {
            return field.getType().getPresentableText().equalsIgnoreCase(fieldToSearch.getType().getPresentableText());
        }

        return Objects.equals(
                field.getName(),
                fieldToSearch.getName()
        );
    }
}
