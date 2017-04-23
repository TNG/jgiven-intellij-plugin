package com.tngtech.jgiven.reference;

import com.google.common.collect.Iterables;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usageView.UsageInfo;
import com.tngtech.jgiven.BaseTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class ReferenceProviderTest extends BaseTestCase {
    public void test_find_Usages_for_type() throws Exception {
        configureByFile("ForClass.java");

        Collection<UsageInfo> usages = findUsages();

        assertThat(getOnlyUsageFilename(usages)).contains("SomeOtherReference.java");
    }

    public void test_find_Usages_for_name() throws Exception {
        configureByFile("ForName.java");

        Collection<UsageInfo> usages = findUsages();

        assertThat(getOnlyUsageFilename(usages)).contains("SomeReference.java");
    }

    public void test_find_Usages_for_java_lang_reference_resolves_by_name_and_not_by_type() throws Exception {
        configureByFile("ForJavaLang.java");

        Collection<UsageInfo> usages = findUsages();

        assertThat(getOnlyUsageFilename(usages)).contains("SomeReference.java");
    }

    @NotNull
    private String getOnlyUsageFilename(Collection<UsageInfo> usages) {
        return Iterables.getOnlyElement(usages).getFile().getName();
    }

    @NotNull
    private Collection<UsageInfo> findUsages() {
        PsiElement element = myFixture.getElementAtCaret();
        return myFixture.findUsages(element);
    }

    @Override
    protected String getTestDataSubDirectory() {
        return "findUsages";
    }

    private void configureByFile(String filename) {
        List<String> files = new ArrayList<>();
        files.add(filename);
        files.addAll(getUsageFilesForAllTests());
        String[] asArray = files.toArray(new String[files.size()]);
        PsiFile[] loadedFiles = myFixture.configureByFiles(asArray);
        assertThat(loadedFiles).hasSameSizeAs(files);
    }

    private Set<String> getUsageFilesForAllTests() {
        return Arrays.stream(new File(getTestDataPath() + "/common").listFiles())
                .map(File::getName)
                .map(name -> "common/" + name)
                .collect(Collectors.toSet());
    }
}