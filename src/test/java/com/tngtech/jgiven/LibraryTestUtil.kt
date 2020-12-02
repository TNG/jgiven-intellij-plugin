package com.tngtech.jgiven

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.PsiTestUtil
import com.tngtech.jgiven.annotation.ScenarioState
import java.io.File
import java.util.*

class LibraryTestUtil constructor(private val myModule: Module) {

    private val references = ArrayList<Ref<Library>>()

    private fun addJarContaining(clazz: Class<*>): LibraryTestUtil {
        val path = clazz.protectionDomain.codeSource.location.path
        addLibraryAt(path)
        return this
    }

    fun addJGiven(): LibraryTestUtil {
        return addJarContaining(ScenarioState::class.java)
    }

    private fun addLibraryAt(path: String) {
        val parts = path.split("/".toRegex()).filterNot { it.isEmpty() }.toTypedArray()
        val file = File(path)
        VfsRootAccess.allowRootAccess(myModule, parts[0])
        val fileName = file.name
        ModuleRootModificationUtil.updateModel(myModule) { model -> references.add(Ref.create(PsiTestUtil.addLibrary(model, fileName, file.parent, fileName))) }
    }

    internal fun removeLibraries() {
        WriteCommandAction.runWriteCommandAction(null) {
            val table = LibraryTablesRegistrar.getInstance().getLibraryTable(myModule.project)
            val model = table.modifiableModel
            references.forEach { reference -> model.removeLibrary(reference.get()) }
            model.commit()
        }
    }
}
