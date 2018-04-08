package com.tngtech.jgiven

import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

abstract class BaseTestCase : LightCodeInsightFixtureTestCase() {

    protected lateinit var libraryTestUtil: LibraryTestUtil

    protected abstract val testDataSubDirectory: String

    @Throws(Exception::class)
    public override fun setUp() {
        super.setUp()
        libraryTestUtil = LibraryTestUtil(myModule).addJGiven()
    }

    @Throws(Exception::class)
    public override fun tearDown() {
        libraryTestUtil.removeLibraries()
        super.tearDown()
    }

    override fun getTestDataPath(): String {
        return BaseTestCase::class.java.getResource("/testData/$testDataSubDirectory").path
    }

    override fun getProjectDescriptor(): LightProjectDescriptor {
        return object : LightProjectDescriptor() {
            override fun getSdk(): Sdk? {
                return JavaAwareProjectJdkTableImpl.getInstanceEx().internalJdk
            }
        }
    }
}
