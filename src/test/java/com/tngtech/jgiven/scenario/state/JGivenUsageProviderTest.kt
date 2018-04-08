package com.tngtech.jgiven.scenario.state

import com.intellij.psi.PsiField
import com.intellij.psi.PsiReference
import com.tngtech.jgiven.resolution.ResolutionHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit

class JGivenUsageProviderTest {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var scenarioStateProvider: ScenarioStateAnnotationProvider
    @Mock
    private lateinit var resolutionHandler: ResolutionHandler
    @Mock
    private lateinit var referenceFactory: ReferenceFactory
    @Mock
    private lateinit var fieldToSearch: PsiField
    @Mock
    private lateinit var field: PsiField

    @InjectMocks
    private lateinit var usageProvider: JGivenUsageProvider

    @Test
    fun should_return_usage() {
        val reference = mock(PsiReference::class.java)
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true)
        `when`(resolutionHandler.resolutionMatches(field, fieldToSearch)).thenReturn(true)
        `when`(referenceFactory.referenceFor(field)).thenReturn(reference)

        val result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)

        assertThat(result).isEqualTo(reference)
    }

    @Test
    fun should_return_no_usage_if_field_not_a_scenario_state() {
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(false)

        val result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)

        assertThat(result).isNull()
    }

    @Test
    fun should_return_no_usage_if_resolutions_dont_match() {
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true)
        `when`(resolutionHandler.resolutionMatches(field, fieldToSearch)).thenReturn(false)

        val result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)

        assertThat(result).isNull()
    }

    @Test
    fun should_return_no_usage_if_fields_are_equal() {
        `when`(scenarioStateProvider.isJGivenScenarioState(field)).thenReturn(true)
        `when`(resolutionHandler.resolutionMatches(field, fieldToSearch)).thenReturn(true)

        val result = usageProvider.createReferenceIfJGivenUsage(fieldToSearch, fieldToSearch)

        assertThat(result).isNull()
    }
}