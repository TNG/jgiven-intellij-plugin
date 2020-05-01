package com.tngtech.jgiven.scenario.state

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.tngtech.jgiven.Annotations
import com.tngtech.jgiven.util.AnnotationProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ScenarioStateAnnotationProviderTest {
    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var annotationProvider: AnnotationProvider

    private lateinit var scenarioStateAnnotationProvider: ScenarioStateAnnotationProvider

    @Before
    fun setUp() {
        scenarioStateAnnotationProvider = ScenarioStateAnnotationProvider(annotationProvider)
    }

    @Test
    fun determine_JGiven_ScenarioState() {
        val field = mock(PsiField::class.java)
        `when`<PsiAnnotation>(annotationProvider.findAnnotation(field, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES)).thenReturn(mock(PsiAnnotation::class.java))

        val result = scenarioStateAnnotationProvider.isJGivenScenarioState(field)

        assertThat(result).isTrue()
    }

    @Test
    fun determine_JGiven_if_element_is_not_a_field() {
        val element = mock(PsiElement::class.java)

        val result = scenarioStateAnnotationProvider.isJGivenScenarioState(element)

        assertThat(result).isFalse()
    }

    @Test
    fun determine_JGiven_if_element_is_not_a_scenario_state() {
        val field = mock(PsiField::class.java)
        `when`<PsiAnnotation>(annotationProvider.findAnnotation(field, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES)).thenReturn(null)

        val result = scenarioStateAnnotationProvider.isJGivenScenarioState(field)

        assertThat(result).isFalse()
    }
}
