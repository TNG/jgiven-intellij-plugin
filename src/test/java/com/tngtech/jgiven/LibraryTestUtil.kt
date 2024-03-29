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

class LibraryTestUtil constructor(private val myModule: Module) {

    private val references = ArrayList<Ref<Library>>()

    private fun addJarContaining(clazz: Class<*>): LibraryTestUtil {
        val path = getJarPath(clazz)
        addLibraryAt(path)
        return this
    }

    private fun getJarPath(clazz: Class<*>): String {
        val className = clazz.name.replace('.', '/') + ".class"
        val classPath = clazz.classLoader.getResource(className)?.toString()
                ?: throw IllegalArgumentException("Class $clazz not found")
        return if (classPath.startsWith("jar")) {
            classPath.substringAfter("jar:file:").substringBefore('!')
        } else {
            throw IllegalArgumentException("Class $clazz is not in a jar file")

        }
    }


    fun addJGiven(): LibraryTestUtil {
        try {
            return addJarContaining(ScenarioState::class.java)
        }catch(e: IllegalArgumentException){
            throw IllegalArgumentException("JGiven jar not found. Please add it to the classpath")
        }
    }

    private fun addLibraryAt(path: String) {
        val parts = path.split("/".toRegex()).filterNot { it.isEmpty() }.toTypedArray()
        val file = File(path)
        VfsRootAccess.allowRootAccess(myModule, parts[0])
        val fileName = file.name
        ModuleRootModificationUtil.updateModel(myModule) { model ->
            references.add(Ref.create(PsiTestUtil.addLibrary(
                    model, fileName, file.parent, fileName
            )))
        }
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