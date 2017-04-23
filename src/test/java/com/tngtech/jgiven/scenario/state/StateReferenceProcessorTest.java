package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StateReferenceProcessorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private JGivenUsageProvider usageProvider;
    @Mock
    private PsiField fieldToSearch;

    @Test
    public void should_add_reference() throws Exception {
        PsiField field = mock(PsiField.class);
        StateReferenceProcessor processor = referenceProcessorFor(2);
        PsiReference reference = mock(PsiReference.class);
        when(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)).thenReturn(reference);

        boolean result = processor.process(field);

        assertThat(processor.getResults()).containsOnly(reference);
        assertThat(result).isTrue();
    }

    @Test
    public void should_not_add_reference() throws Exception {
        PsiField field = mock(PsiField.class);
        StateReferenceProcessor processor = referenceProcessorFor(1);
        when(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)).thenReturn(null);

        processor.process(field);

        assertThat(processor.getResults()).isEmpty();
    }

    @Test
    public void should_return_false_if_enough_results_have_been_read() throws Exception {
        PsiField field1 = mock(PsiField.class);
        PsiReference reference1 = mock(PsiReference.class);
        when(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field1)).thenReturn(reference1);
        StateReferenceProcessor processor = referenceProcessorFor(1);

        boolean result = processor.process(field1);

        assertThat(result).isFalse();
    }

    @Test
    public void should_handle_any_number_of_max_results() throws Exception {
        PsiField field1 = mock(PsiField.class);
        PsiReference reference1 = mock(PsiReference.class);
        when(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field1)).thenReturn(reference1);
        StateReferenceProcessor processor = referenceProcessorFor(ScenarioStateReferenceProvider.ANY_NUMBER_OF_RESULTS);

        boolean result = processor.process(field1);

        assertThat(result).isTrue();
    }

    private StateReferenceProcessor referenceProcessorFor(int maxNumberOfResults) {
        return new StateReferenceProcessor(fieldToSearch, maxNumberOfResults, usageProvider);
    }
}