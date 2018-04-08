package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResolutionProviderTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ScenarioStateAnnotationProvider scenarioStateProvider;
    @Mock
    private AnnotationValueProvider annotationValueProvider;
    @Mock
    private TypeIsTooGenericCalculator typeIsTooGenericCalculator;
    @Mock
    private PsiField field;

    private ResolutionProvider resolutionProvider;

    @Before
    public void setUp() {
        when(field.getType()).thenReturn(mock(PsiType.class));
        resolutionProvider = new ResolutionProvider(scenarioStateProvider, annotationValueProvider, typeIsTooGenericCalculator);
    }

    @Test
    public void handle_NAME_resolution() {
        mockAnnotationValueWith("NAME", field);

        Resolution result = resolutionProvider.getResolutionFrom(field);

        assertThat(result).isEqualTo(Resolution.NAME);
    }

    @Test
    public void handle_TYPE_resolution() {
        mockAnnotationValueWith("TYPE", field);

        Resolution result = resolutionProvider.getResolutionFrom(field);

        assertThat(result).isEqualTo(Resolution.TYPE);
    }

    @Test
    public void handle_AUTO_resolution_not_being_too_generic() {
        mockAnnotationValueWith("AUTO", field);
        when(typeIsTooGenericCalculator.typeIsTooGeneric(field.getType())).thenReturn(false);

        Resolution result = resolutionProvider.getResolutionFrom(field);

        assertThat(result).isEqualTo(Resolution.TYPE);
    }

    @Test
    public void handle_AUTO_resolution_being_too_generic() {
        mockAnnotationValueWith("AUTO", field);
        when(typeIsTooGenericCalculator.typeIsTooGeneric(field.getType())).thenReturn(true);

        Resolution result = resolutionProvider.getResolutionFrom(field);

        assertThat(result).isEqualTo(Resolution.NAME);
    }

    @Test
    public void handle_not_present_resolution_being_too_generic() {
        mockAnnotationValueWith(field, null);
        when(typeIsTooGenericCalculator.typeIsTooGeneric(field.getType())).thenReturn(true);

        Resolution result = resolutionProvider.getResolutionFrom(field);

        assertThat(result).isEqualTo(Resolution.NAME);
    }

    @Test
    public void handle_not_present_resolution_not_being_too_generic() {
        mockAnnotationValueWith(field, null);
        when(typeIsTooGenericCalculator.typeIsTooGeneric(field.getType())).thenReturn(false);

        Resolution result = resolutionProvider.getResolutionFrom(field);

        assertThat(result).isEqualTo(Resolution.TYPE);
    }

    private void mockAnnotationValueWith(String name, PsiField field) {
        PsiExpression annotationValueExpression = mock(PsiExpression.class);
        when(annotationValueExpression.getText()).thenReturn(name);
        mockAnnotationValueWith(field, annotationValueExpression);
    }

    private void mockAnnotationValueWith(PsiField field, PsiExpression valueExpression) {
        PsiAnnotation annotation = mock(PsiAnnotation.class);
        when(scenarioStateProvider.getJGivenAnnotationOn(field)).thenReturn(annotation);
        when(annotationValueProvider.getAnnotationValue(annotation, ResolutionProvider.FIELD_RESOLUTION)).thenReturn(valueExpression);
    }
}