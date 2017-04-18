package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiNameValuePair;

import javax.annotation.Nullable;
import java.util.Arrays;

public class AnnotationUtils {

    @Nullable
    public static PsiExpression getAnnotationValue(PsiAnnotation annotation, String annotationKey) {
        PsiNameValuePair[] attributes = annotation.getParameterList().getAttributes();
        return Arrays.stream(attributes)
                .filter(a -> annotationKey.equalsIgnoreCase(a.getName()))
                .map(PsiNameValuePair::getValue)
                .filter(v -> v instanceof PsiExpression)
                .map(v -> (PsiExpression) v)
                .findFirst().orElse(null);
    }
}
