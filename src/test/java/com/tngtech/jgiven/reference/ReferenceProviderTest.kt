package com.tngtech.jgiven.reference

import com.intellij.mock.MockApplication
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiField
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.*

class ReferenceProviderTest {
    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var scenarioStateProvider: ScenarioStateAnnotationProvider
    @Mock
    private lateinit var scenarioStateReferenceProvider: ScenarioStateReferenceProvider
    @Mock
    private lateinit var processor: Processor<PsiReference>

    private lateinit var referenceProvider: ReferenceProvider

    @Before
    fun setUp() {
        val disposable = mock(Disposable::class.java)
        ApplicationManager.setApplication(MockApplication(disposable), disposable)

        referenceProvider = ReferenceProvider(scenarioStateProvider, scenarioStateReferenceProvider)
    }

    @Test
    fun should_process_reference() {
        // given
        val reference1 = mock(PsiReference::class.java)
        val reference2 = mock(PsiReference::class.java)

        val field = mock(PsiField::class.java)
        val searchParameters = mock(ReferencesSearch.SearchParameters::class.java)
        `when`(searchParameters.elementToSearch).thenReturn(field)
        `when`(searchParameters.effectiveSearchScope).thenReturn(mock(GlobalSearchScope::class.java))
        `when`(scenarioStateReferenceProvider.findReferences(field)).thenReturn(Arrays.asList(reference1, reference2))
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true)

        // when
        referenceProvider.processQuery(searchParameters, processor)

        // then
        verify(processor).process(reference1)
        verify(processor).process(reference2)
    }

    @Test
    fun should_not_process_reference_if_search_scope_is_not_global() {
        // given
        val field = mock(PsiField::class.java)
        val searchParameters = mock(ReferencesSearch.SearchParameters::class.java)
        `when`(searchParameters.elementToSearch).thenReturn(field)
        `when`(searchParameters.effectiveSearchScope).thenReturn(mock(LocalSearchScope::class.java))
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true)

        // when
        referenceProvider.processQuery(searchParameters, processor)

        // then
        verifyNoInteractions(processor)
    }

    @Test
    fun should_not_process_reference_if_element_is_not_a_JGiven_scenario_state() {
        // given
        val field = mock(PsiField::class.java)
        val searchParameters = mock(ReferencesSearch.SearchParameters::class.java)
        `when`(searchParameters.elementToSearch).thenReturn(field)
        `when`(searchParameters.effectiveSearchScope).thenReturn(mock(GlobalSearchScope::class.java))
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(false)

        // when
        referenceProvider.processQuery(searchParameters, processor)

        // then
        verifyNoInteractions(processor)
    }
}
