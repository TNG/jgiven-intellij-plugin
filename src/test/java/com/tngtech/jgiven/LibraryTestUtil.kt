package com.tngtech.jgiven

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.PsiTestUtil
import com.tngtech.jgiven.annotation.ScenarioState
import java.io.File
import java.util.*

class LibraryTestUtil internal constructor(private val myModule: Module) {

    private val references = ArrayList<Ref<Library>>()

    fun addJarContaining(clazz: Class<*>): LibraryTestUtil {
        val path = clazz.protectionDomain.codeSource.location.path
        addLibraryAt(path)
        return this
    }

    fun addJGiven(): LibraryTestUtil {
        return addJarContaining(ScenarioState::class.java)
    }

    private fun addLibraryAt(path: String) {
        val parts = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val file = File(path)
        VfsRootAccess.allowRootAccess(parts[0])
        val fileName = file.name

        ModuleRootModificationUtil.updateModel(myModule) { model -> references.add(Ref.create(PsiTestUtil.addLibrary(myModule, model, fileName, file.parent, fileName))) }
    }

    internal fun removeLibraries() {
        WriteCommandAction.runWriteCommandAction(null) {
            val table = ProjectLibraryTable.getInstance(myModule.project)
            val model = table.modifiableModel
            references.forEach { reference -> model.removeLibrary(reference.get()) }
            model.commit()
        }
    }
}
