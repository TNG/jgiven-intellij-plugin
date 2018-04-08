package com.tngtech.jgiven.resolution

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiType
import com.tngtech.java.junit.dataprovider.DataProvider
import com.tngtech.java.junit.dataprovider.DataProviderRunner
import com.tngtech.java.junit.dataprovider.UseDataProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.BufferedReader
import java.util.stream.Stream

@RunWith(DataProviderRunner::class)
class TypeIsTooGenericCalculatorTest {

    @Test
    @UseDataProvider("tooComplexProvider")
    fun should_determine_whether_type_is_too_complex(testCase: TestCase) {
        mockPsiClassWith(testCase.typeMock, testCase.qualifiedName)

        val result = TypeIsTooGenericCalculator().typeIsTooGeneric(testCase.typeMock)

        assertThat(result).isEqualTo(testCase.expectedResult)
    }

    private fun mockPsiClassWith(psiType: PsiType?, qualifiedName: String?) {
        if (psiType is PsiClassType) {
            val clazz = mock(PsiClass::class.java)
            `when`<String>(clazz.qualifiedName).thenReturn(qualifiedName)
            `when`<PsiClass>(psiType.resolve()).thenReturn(clazz)
        }
    }

    class TestCase(
            val typeMock: PsiType,
            val qualifiedName: String?,
            val expectedResult: Boolean
    ) {
        override fun toString(): String {
            return "TestCase{" +
                    "qualifiedName='" + qualifiedName + '\''.toString() +
                    ", expectedResult=" + expectedResult +
                    '}'.toString()
        }
    }

    companion object {
        @Suppress("unused")
        @DataProvider
        @JvmStatic
        fun tooComplexProvider() = listOf(
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = String::class.java.name,
                        expectedResult = true),
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = Integer::class.java.name,
                        expectedResult = true),
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = List::class.java.name,
                        expectedResult = true),
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = Stream::class.java.name,
                        expectedResult = true),
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = BufferedReader::class.java.name,
                        expectedResult = true),
                TestCase(typeMock = mock(PsiType::class.java),
                        qualifiedName = "some_qualified_name",
                        expectedResult = true),
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = null,
                        expectedResult = true),
                TestCase(typeMock = mock(PsiClassType::class.java),
                        qualifiedName = "myClass",
                        expectedResult = false)
        )
    }
}
