package com.tngtech.jgiven;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.testFramework.PsiTestUtil;
import com.tngtech.jgiven.annotation.ScenarioState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryTestUtil {
    private final Module myModule;

    private List<Ref<Library>> references = new ArrayList<>();

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
        String[] parts = path.split("/");
        File file = new File(path);
        VfsRootAccess.allowRootAccess(parts[0]);
        String fileName = file.getName();

        ModuleRootModificationUtil.updateModel(myModule, model ->
                references.add(Ref.create(PsiTestUtil.addLibrary(myModule, model, fileName, file.getParent(), fileName))));
    }

    void removeLibraries() {
        WriteCommandAction.runWriteCommandAction(null, () -> {
            LibraryTable table = ProjectLibraryTable.getInstance(myModule.getProject());
            LibraryTable.ModifiableModel model = table.getModifiableModel();
            references.forEach(reference -> model.removeLibrary(reference.get()));
            model.commit();
        });
    }
}
