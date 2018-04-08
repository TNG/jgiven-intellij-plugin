package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.tngtech.jgiven.Annotations;
import com.tngtech.jgiven.util.AnnotationProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioStateAnnotationProviderTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AnnotationProvider annotationProvider;

    @InjectMocks
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider;

    @Before
    public void setUp() {
        scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider(annotationProvider);
    }

    @Test
    public void determine_JGiven_ScenarioState() {
        PsiField field = mock(PsiField.class);
        when(annotationProvider.findAnnotation(field, Annotations.INSTANCE.getJGIVEN_SCENARIO_STATE_CLASS_NAMES())).thenReturn(mock(PsiAnnotation.class));

        boolean result = scenarioStateAnnotationProvider.isJGivenScenarioState(field);

        assertThat(result).isTrue();
    }

    @Test
    public void determine_JGiven_if_element_is_not_a_field() {
        PsiElement element = mock(PsiElement.class);

        boolean result = scenarioStateAnnotationProvider.isJGivenScenarioState(element);

        assertThat(result).isFalse();
    }

    @Test
    public void determine_JGiven_if_element_is_not_a_scenario_state() {
        PsiField field = mock(PsiField.class);
        when(annotationProvider.findAnnotation(field, Annotations.INSTANCE.getJGIVEN_SCENARIO_STATE_CLASS_NAMES())).thenReturn(null);

        boolean result = scenarioStateAnnotationProvider.isJGivenScenarioState(field);

        assertThat(result).isFalse();
    }
}