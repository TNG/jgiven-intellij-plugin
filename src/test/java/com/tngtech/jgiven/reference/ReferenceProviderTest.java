package com.tngtech.jgiven.reference;

import com.intellij.mock.MockApplication;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ReferenceProviderTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ScenarioStateAnnotationProvider scenarioStateProvider = mock(ScenarioStateAnnotationProvider.class);
    @Mock
    private ScenarioStateReferenceProvider scenarioStateReferenceProvider = mock(ScenarioStateReferenceProvider.class);
    @Mock
    private Processor<PsiReference> processor;

    private ReferenceProvider referenceProvider;

    @Before
    public void setUp() {
        Disposable disposable = mock(Disposable.class);
        ApplicationManager.setApplication(new MockApplication(disposable), disposable);

        referenceProvider = new ReferenceProvider(scenarioStateProvider, scenarioStateReferenceProvider);
    }

    @Test
    public void should_process_reference() {
        // given
        PsiReference reference1 = mock(PsiReference.class);
        PsiReference reference2 = mock(PsiReference.class);

        PsiField field = mock(PsiField.class);
        ReferencesSearch.SearchParameters searchParameters = mock(ReferencesSearch.SearchParameters.class);
        when(searchParameters.getElementToSearch()).thenReturn(field);
        when(searchParameters.getEffectiveSearchScope()).thenReturn(mock(GlobalSearchScope.class));
        when(scenarioStateReferenceProvider.findReferences(eq(field), anyInt())).thenReturn(Arrays.asList(reference1, reference2));
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true);

        // when
        referenceProvider.processQuery(searchParameters, processor);

        // then
        verify(processor).process(reference1);
        verify(processor).process(reference2);
    }

    @Test
    public void should_not_process_reference_if_search_scope_is_not_global() {
        // given
        PsiField field = mock(PsiField.class);
        ReferencesSearch.SearchParameters searchParameters = mock(ReferencesSearch.SearchParameters.class);
        when(searchParameters.getElementToSearch()).thenReturn(field);
        when(searchParameters.getEffectiveSearchScope()).thenReturn(mock(LocalSearchScope.class));
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true);

        // when
        referenceProvider.processQuery(searchParameters, processor);

        // then
        verifyZeroInteractions(processor);
    }

    @Test
    public void should_not_process_reference_if_element_is_not_a_JGiven_scenario_state() {
        // given
        PsiField field = mock(PsiField.class);
        ReferencesSearch.SearchParameters searchParameters = mock(ReferencesSearch.SearchParameters.class);
        when(searchParameters.getElementToSearch()).thenReturn(field);
        when(searchParameters.getEffectiveSearchScope()).thenReturn(mock(GlobalSearchScope.class));
        when(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(false);

        // when
        referenceProvider.processQuery(searchParameters, processor);

        // then
        verifyZeroInteractions(processor);
    }
}