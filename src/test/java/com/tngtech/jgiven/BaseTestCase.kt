package com.tngtech.jgiven

import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase

abstract class BaseTestCase : LightJavaCodeInsightFixtureTestCase() {

    private lateinit var libraryTestUtil: LibraryTestUtil

    protected abstract val testDataSubDirectory: String

    @Throws(Exception::class)
    public override fun setUp() {
        super.setUp()
        libraryTestUtil = LibraryTestUtil(module).addJGiven()
    }

    @Throws(Exception::class)
    public override fun tearDown() {
        libraryTestUtil.removeLibraries()
        super.tearDown()
    }

    override fun getTestDataPath(): String {
        return BaseTestCase::class.java.getResource("/testData/$testDataSubDirectory").path
    }

    override fun getProjectDescriptor() = LightProjectDescriptor()
}
