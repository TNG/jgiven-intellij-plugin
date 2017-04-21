package com.tngtech.jgiven.reference;

import com.google.common.collect.Iterables;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.usageView.UsageInfo;
import com.tngtech.jgiven.BaseTestCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class ReferenceProviderTest extends BaseTestCase {
    public void test_find_Usages_for_type() throws Exception {
        libraryTestUtil.addJGiven();
        PsiFile[] files = myFixture.configureByFiles("ForClass.java", "StateA.java", "ForClassTypeReference.java",
                "ForNameReference.java");
        PsiElement element = myFixture.getElementAtCaret();
        CodeInsightTestFixtureImpl codeInsightTestFixture = (CodeInsightTestFixtureImpl) myFixture;
        Set<VirtualFile> virtualFiles = Arrays.stream(files).map(PsiFile::getVirtualFile).collect(Collectors.toSet());

        Collection<UsageInfo> usages = codeInsightTestFixture.findUsages(element, GlobalSearchScope.filesScope(getProject(), virtualFiles));

        assertThat(Iterables.getOnlyElement(usages).getFile().getName())
                .contains("ForClassTypeReference.java");
    }

    public void test_find_Usages_for_name() throws Exception {
        libraryTestUtil.addJGiven();
        PsiFile[] files = myFixture.configureByFiles("ForName.java", "StateA.java", "ForClassTypeReference.java",
                "ForNameReference.java");
        PsiElement element = myFixture.getElementAtCaret();
        CodeInsightTestFixtureImpl codeInsightTestFixture = (CodeInsightTestFixtureImpl) myFixture;
        Set<VirtualFile> virtualFiles = Arrays.stream(files).map(PsiFile::getVirtualFile).collect(Collectors.toSet());

        Collection<UsageInfo> usages = codeInsightTestFixture.findUsages(element, GlobalSearchScope.filesScope(getProject(), virtualFiles));

        assertThat(Iterables.getOnlyElement(usages).getFile().getName())
                .contains("ForNameReference.java");
    }

    @Override
    protected String getTestDataSubDirectory() {
        return "findUsages";
    }
}