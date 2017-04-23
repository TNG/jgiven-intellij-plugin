package com.tngtech.jgiven.reference;

import com.intellij.psi.PsiField;
import com.intellij.usages.impl.rules.UsageType;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JGivenUsageTypeProviderTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider;
    @InjectMocks
    private JGivenUsageTypeProvider usageTypeProvider;

    @Test
    public void provides_JGiven_Usage_type_if_field_is_scenario_state() throws Exception {
        PsiField field = mock(PsiField.class);
        when(scenarioStateAnnotationProvider.isJGivenScenarioState(field)).thenReturn(true);

        UsageType type = usageTypeProvider.getUsageType(field);

        assertThat(type).isNotNull();
        assertThat(type.toString()).isEqualTo(JGivenUsageTypeProvider.USAGE_TYPE);
    }

    @Test
    public void provides_no_Usage_type_if_field_is_not_scenario_state() throws Exception {
        PsiField field = mock(PsiField.class);
        when(scenarioStateAnnotationProvider.isJGivenScenarioState(field)).thenReturn(false);

        UsageType type = usageTypeProvider.getUsageType(field);

        assertThat(type).isNull();
    }
}