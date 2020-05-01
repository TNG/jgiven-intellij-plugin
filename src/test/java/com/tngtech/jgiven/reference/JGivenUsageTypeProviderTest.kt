package com.tngtech.jgiven.reference

import com.intellij.psi.PsiField
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class JGivenUsageTypeProviderTest {
    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var scenarioStateAnnotationProvider: ScenarioStateAnnotationProvider

    @Mock
    private lateinit var usageTypeProvider: JGivenUsageTypeProvider

    @Before
    fun setUp() {
        usageTypeProvider = JGivenUsageTypeProvider(scenarioStateAnnotationProvider)
    }

    @Test
    fun provides_JGiven_Usage_type_if_field_is_scenario_state() {
        val field = mock(PsiField::class.java)
        `when`(scenarioStateAnnotationProvider.isJGivenScenarioState(field)).thenReturn(true)

        val type = usageTypeProvider.getUsageType(field)

        assertThat(type).isNotNull
        assertThat(type).isEqualTo(JGivenUsageTypeProvider.USAGE_TYPE)
    }

    @Test
    fun provides_no_Usage_type_if_field_is_not_scenario_state() {
        val field = mock(PsiField::class.java)
        `when`(scenarioStateAnnotationProvider.isJGivenScenarioState(field)).thenReturn(false)

        val type = usageTypeProvider.getUsageType(field)

        assertThat(type).isNull()
    }
}
