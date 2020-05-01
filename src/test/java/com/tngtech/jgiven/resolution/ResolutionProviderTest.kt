package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiField
import com.intellij.psi.PsiType
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

class ResolutionProviderTest {

    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var scenarioStateProvider: ScenarioStateAnnotationProvider
    @Mock
    private lateinit var annotationValueProvider: AnnotationValueProvider
    @Mock
    private lateinit var typeIsTooGenericCalculator: TypeIsTooGenericCalculator
    @Mock
    private lateinit var field: PsiField

    private lateinit var resolutionProvider: ResolutionProvider

    @Before
    fun setUp() {
        `when`(field.type).thenReturn(mock(PsiType::class.java))
        resolutionProvider = ResolutionProvider(scenarioStateProvider, annotationValueProvider, typeIsTooGenericCalculator)
    }

    @Test
    fun handle_NAME_resolution() {
        mockAnnotationValueWith("NAME", field)

        val result = resolutionProvider.getResolutionFrom(field)

        assertThat(result).isEqualTo(Resolution.NAME)
    }

    @Test
    fun handle_TYPE_resolution() {
        mockAnnotationValueWith("TYPE", field)

        val result = resolutionProvider.getResolutionFrom(field)

        assertThat(result).isEqualTo(Resolution.TYPE)
    }

    @Test
    fun handle_AUTO_resolution_not_being_too_generic() {
        mockAnnotationValueWith("AUTO", field)
        `when`(typeIsTooGenericCalculator.typeIsTooGeneric(field.type)).thenReturn(false)

        val result = resolutionProvider.getResolutionFrom(field)

        assertThat(result).isEqualTo(Resolution.TYPE)
    }

    @Test
    fun handle_AUTO_resolution_being_too_generic() {
        mockAnnotationValueWith("AUTO", field)
        `when`(typeIsTooGenericCalculator.typeIsTooGeneric(field.type)).thenReturn(true)

        val result = resolutionProvider.getResolutionFrom(field)

        assertThat(result).isEqualTo(Resolution.NAME)
    }

    @Test
    fun handle_not_present_resolution_being_too_generic() {
        mockAnnotationValueWith(field, null)
        `when`(typeIsTooGenericCalculator.typeIsTooGeneric(field.type)).thenReturn(true)

        val result = resolutionProvider.getResolutionFrom(field)

        assertThat(result).isEqualTo(Resolution.NAME)
    }

    @Test
    fun handle_not_present_resolution_not_being_too_generic() {
        mockAnnotationValueWith(field, null)
        `when`(typeIsTooGenericCalculator.typeIsTooGeneric(field.type)).thenReturn(false)

        val result = resolutionProvider.getResolutionFrom(field)

        assertThat(result).isEqualTo(Resolution.TYPE)
    }

    private fun mockAnnotationValueWith(name: String, field: PsiField) {
        val annotationValueExpression = mock(PsiExpression::class.java)
        `when`(annotationValueExpression.text).thenReturn(name)
        mockAnnotationValueWith(field, annotationValueExpression)
    }

    private fun mockAnnotationValueWith(field: PsiField, valueExpression: PsiExpression?) {
        val annotation = mock(PsiAnnotation::class.java)
        `when`<PsiAnnotation>(scenarioStateProvider.getJGivenAnnotationOn(field)).thenReturn(annotation)
        `when`<PsiExpression>(annotationValueProvider.getAnnotationValue(annotation, ResolutionProvider.FIELD_RESOLUTION)).thenReturn(valueExpression)
    }
}
