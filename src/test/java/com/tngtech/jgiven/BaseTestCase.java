package com.tngtech.jgiven;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseTestCase extends LightCodeInsightFixtureTestCase {

    protected LibraryTestUtil libraryTestUtil;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        libraryTestUtil = new LibraryTestUtil(myModule).addJGiven();
    }

    @Override
    public void tearDown() throws Exception {
        libraryTestUtil.removeLibraries();
        super.tearDown();
    }

    @Override
    protected String getTestDataPath() {
        return BaseTestCase.class.getResource("/testData/" + getTestDataSubDirectory()).getPath();
    }

    protected abstract String getTestDataSubDirectory();

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new LightProjectDescriptor() {
            @Override
            public Sdk getSdk() {
                return JavaAwareProjectJdkTableImpl.getInstanceEx().getInternalJdk();
            }
        };
    }
}
