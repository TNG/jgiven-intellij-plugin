package com.tngtech.jgiven.reference

import com.google.common.collect.Iterables
import com.intellij.usageView.UsageInfo
import com.tngtech.jgiven.BaseTestCase
import org.assertj.core.api.Assertions.assertThat
import java.io.File

class ScenarioStateUsageProviderIntegrationTest : BaseTestCase() {

    private val usageFilesForAllTests: Set<String>
        get() = File("$testDataPath/common").listFiles()!!
                .map { it.name }
                .map { name -> "common/$name" }
                .toSet()

    fun test_find_Usages_for_type() {
        configureByFile("ForClass.java")

        val usages = findUsages()

        assertThat(getOnlyUsageFilename(usages)).contains("SomeOtherReference.java")
    }

    fun test_find_Usages_for_name() {
        configureByFile("ForName.java")

        val usages = findUsages()

        assertThat(getOnlyUsageFilename(usages)).contains("SomeReference.java")
    }

    fun test_find_Usages_for_java_lang_reference_resolves_by_name_and_not_by_type() {
        configureByFile("ForJavaLang.java")

        val usages = findUsages()

        assertThat(getOnlyUsageFilename(usages)).contains("SomeReference.java")
    }

    fun test_find_no_usages_if_JGiven_is_not_present() {
        myFixture.configureByFile("NoJGiven.java")

        val usages = findUsages()

        assertThat(usages).isEmpty()
    }

    private fun getOnlyUsageFilename(usages: Collection<UsageInfo>) =
            Iterables.getOnlyElement(usages).file!!.name

    private fun findUsages(): Collection<UsageInfo> {
        val element = myFixture.elementAtCaret
        return myFixture.findUsages(element)
    }

    override val testDataSubDirectory = "findUsages"

    private fun configureByFile(filename: String) {
        val files = listOf(filename) + usageFilesForAllTests
        val loadedFiles = myFixture.configureByFiles(*files.toTypedArray())
        assertThat(loadedFiles).hasSameSizeAs(files)
    }
}