package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Stream;

import static com.tngtech.java.junit.dataprovider.DataProviders.testForEach;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class TypeIsTooGenericCalculatorTest {
    @DataProvider
    public static Object[][] tooComplexProvider() {
        return testForEach(
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedNameOf(String.class).thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedNameOf(Integer.class).thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedNameOf(List.class).thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedNameOf(Stream.class).thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedNameOf(BufferedReader.class).thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiType.class))
                        .withQualifiedName("some_qualified_name").thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedName(null).thenExpectTheResult().toBeTooGeneric(),
                new TestCase().withTypeMock(mock(PsiClassType.class))
                        .withQualifiedName("myClass").thenExpectTheResult().toBeNotTooGeneric()
        );
    }

    @Test
    @UseDataProvider("tooComplexProvider")
    public void should_determe_whether_type_is_too_complex(TestCase testCase) {
        mockPsiClassWith(testCase.typeMock, testCase.qualifiedName);

        boolean result = new TypeIsTooGenericCalculator().typeIsTooGeneric(testCase.typeMock);

        assertThat(result).isEqualTo(testCase.expectedResult);
    }

    private void mockPsiClassWith(PsiType psiType, String qualifiedName) {
        if (psiType instanceof PsiClassType) {
            PsiClass clazz = mock(PsiClass.class);
            when(clazz.getQualifiedName()).thenReturn(qualifiedName);
            when(((PsiClassType) psiType).resolve()).thenReturn(clazz);
        }
    }

    private static class TestCase {
        private PsiType typeMock;
        private String qualifiedName;
        private boolean expectedResult;

        TestCase withTypeMock(PsiType psiType) {
            this.typeMock = psiType;
            return this;
        }

        TestCase withQualifiedName(String qualifiedName) {
            this.qualifiedName = qualifiedName;
            return this;
        }

        TestCase withQualifiedNameOf(Class<?> clazz) {
            return withQualifiedName(clazz.getName());
        }

        TestCase thenExpectTheResult() {
            return this;
        }

        TestCase toBeTooGeneric() {
            this.expectedResult = true;
            return this;
        }

        TestCase toBeNotTooGeneric() {
            this.expectedResult = false;
            return this;
        }

        @Override
        public String toString() {
            return "TestCase{" +
                    "qualifiedName='" + qualifiedName + '\'' +
                    ", expectedResult=" + expectedResult +
                    '}';
        }
    }
}
