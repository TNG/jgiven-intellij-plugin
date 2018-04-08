package com.tngtech.jgiven

import com.intellij.openapi.util.IconLoader

object Icons {
    private const val JGIVEN_PNG = "/icons/jgiven.png"
    val JGIVEN = load(JGIVEN_PNG)

    private fun load(path: String) = IconLoader.getIcon(path, Icons::class.java)
}
