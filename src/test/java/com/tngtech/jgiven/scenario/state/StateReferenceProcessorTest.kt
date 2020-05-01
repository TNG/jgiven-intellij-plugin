package com.tngtech.jgiven.scenario.state

import com.intellij.psi.PsiField
import com.intellij.psi.PsiReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class StateReferenceProcessorTest {

    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var usageProvider: JGivenUsageProvider
    @Mock
    private lateinit var fieldToSearch: PsiField

    @Test
    fun should_add_reference() {
        val field = mock(PsiField::class.java)
        val processor = referenceProcessorFor(2)
        val reference = mock(PsiReference::class.java)
        `when`<PsiReference>(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)).thenReturn(reference)

        val result = processor.process(field)

        assertThat(processor.getResults()).containsOnly(reference)
        assertThat(result).isTrue()
    }

    @Test
    fun should_not_add_reference() {
        val field = mock(PsiField::class.java)
        val processor = referenceProcessorFor(1)
        `when`<PsiReference>(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field)).thenReturn(null)

        processor.process(field)

        assertThat(processor.getResults()).isEmpty()
    }

    @Test
    fun should_return_false_if_enough_results_have_been_read() {
        val field1 = mock(PsiField::class.java)
        val reference1 = mock(PsiReference::class.java)
        `when`<PsiReference>(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field1)).thenReturn(reference1)
        val processor = referenceProcessorFor(1)

        val result = processor.process(field1)

        assertThat(result).isFalse()
    }

    @Test
    fun should_handle_any_number_of_max_results() {
        val field1 = mock(PsiField::class.java)
        val reference1 = mock(PsiReference::class.java)
        `when`<PsiReference>(usageProvider.createReferenceIfJGivenUsage(fieldToSearch, field1)).thenReturn(reference1)
        val processor = referenceProcessorFor(ScenarioStateReferenceProvider.ANY_NUMBER_OF_RESULTS)

        val result = processor.process(field1)

        assertThat(result).isTrue()
    }

    private fun referenceProcessorFor(maxNumberOfResults: Int): StateReferenceProcessor {
        return StateReferenceProcessor(fieldToSearch, maxNumberOfResults, usageProvider)
    }
}
