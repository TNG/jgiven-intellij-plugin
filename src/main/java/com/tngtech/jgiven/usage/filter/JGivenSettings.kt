package com.tngtech.jgiven.usage.filter

import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@State(name = "JGivenSettings", storages = [Storage("JGivenSettings.xml")])
class JGivenSettings {
    var isJGivenFilteringEnabled = true

    companion object {
        val instance: JGivenSettings
            get() = service()
    }
}
