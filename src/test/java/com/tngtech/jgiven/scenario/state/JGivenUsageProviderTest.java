package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.tngtech.jgiven.resolution.ResolutionHandler;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JGivenUsageProviderTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ScenarioStateAnnotationProvider scenarioStateProvider;
    @Mock
    private ResolutionHandler resolutionHandler;
    @Mock
    private ReferenceFactory referenceFactory;
    @Mock
    private PsiField fieldToSearch;
    @Mock
    private PsiField field;

    @InjectMocks
    private JGivenUsageProvider usageProvider;

    @Test
    public void should_return_usage() {
        PsiReference reference = mock(PsiReference.class);
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true);
        when(resolutionHandler.resolutionMatches(field, fieldToSearch)).thenReturn(true);
        when(referenceFactory.referenceFor(field)).thenReturn(reference);

        PsiReference result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field);

        assertThat(result).isEqualTo(reference);
    }

    @Test
    public void should_return_no_usage_if_field_not_a_scenario_state() {
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(false);

        PsiReference result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field);

        assertThat(result).isNull();
    }

    @Test
    public void should_return_no_usage_if_resolutions_dont_match() {
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true);
        when(resolutionHandler.resolutionMatches(field, fieldToSearch)).thenReturn(false);

        PsiReference result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field);

        assertThat(result).isNull();
    }

    @Test
    public void should_return_no_usage_if_fields_are_equal() {
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true);
        when(resolutionHandler.resolutionMatches(field, fieldToSearch)).thenReturn(true);

        PsiReference result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, fieldToSearch);

        assertThat(result).isNull();
    }
}