package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiField
import com.intellij.psi.PsiType
import com.tngtech.jgiven.resolution.Resolution.NAME
import com.tngtech.jgiven.resolution.Resolution.TYPE
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ResolutionHandlerTest {

    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var resolutionProvider: ResolutionProvider
    @Mock
    private lateinit var field1: PsiField
    @Mock
    private lateinit var field2: PsiField

    private lateinit var resolutionHandler: ResolutionHandler

    @Before
    fun setUp() {
        resolutionHandler = ResolutionHandler(resolutionProvider)
    }

    @Test
    fun should_handle_equal_field_names() {
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field1)).thenReturn(NAME)
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field2)).thenReturn(NAME)
        `when`(field1.name).thenReturn("field")
        `when`(field2.name).thenReturn("field")

        val result = resolutionHandler.resolutionMatches(field1, field2)

        assertThat(result).isTrue()
    }

    @Test
    fun should_handle_not_equal_field_names() {
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field1)).thenReturn(NAME)
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field2)).thenReturn(NAME)
        `when`(field1.name).thenReturn("field1")
        `when`(field2.name).thenReturn("field2")

        val result = resolutionHandler.resolutionMatches(field1, field2)

        assertThat(result).isFalse()
    }

    @Test
    fun should_handle_equal_types() {
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field1)).thenReturn(TYPE)
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field2)).thenReturn(TYPE)
        mockPresentableTextWith(field1, "type")
        mockPresentableTextWith(field2, "type")

        val result = resolutionHandler.resolutionMatches(field1, field2)

        assertThat(result).isTrue()
    }

    @Test
    fun should_handle_not_equal_types() {
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field1)).thenReturn(TYPE)
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field2)).thenReturn(TYPE)
        mockPresentableTextWith(field1, "type1")
        mockPresentableTextWith(field2, "type2")

        val result = resolutionHandler.resolutionMatches(field1, field2)

        assertThat(result).isFalse()
    }

    @Test
    fun should_handle_different_resolution() {
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field1)).thenReturn(NAME)
        `when`<Resolution>(resolutionProvider.getResolutionFrom(field2)).thenReturn(TYPE)

        val result = resolutionHandler.resolutionMatches(field1, field2)

        assertThat(result).isFalse()
    }

    private fun mockPresentableTextWith(field: PsiField, text: String) {
        val type = mock(PsiType::class.java)
        `when`(field.type).thenReturn(type)
        `when`(type.presentableText).thenReturn(text)
    }
}
