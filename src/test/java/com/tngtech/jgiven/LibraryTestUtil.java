package com.tngtech.jgiven;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.testFramework.PsiTestUtil;
import com.tngtech.jgiven.annotation.ScenarioState;

public class LibraryTestUtil {
    private final Module myModule;

    LibraryTestUtil(Module myModule) {
        this.myModule = myModule;
    }

    public LibraryTestUtil addJarContaining(Class<?> clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        addLibraryAt(path);
        return this;
    }

    public LibraryTestUtil addJGiven() {
        return addJarContaining(ScenarioState.class);
    }

    private void addLibraryAt(String path) {
        VfsRootAccess.allowRootAccess(path.split("/")[0]);
        PsiTestUtil.addLibrary(myModule, path);
    }
}
