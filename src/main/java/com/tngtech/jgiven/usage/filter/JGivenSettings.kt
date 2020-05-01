package com.tngtech.jgiven.usage.filter;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

@State(
        name = "JGivenSettings",
        storages = {
                @Storage("JGivenSettings.xml")
        }
)
public class JGivenSettings {
    private boolean isJGivenFilteringEnabled = true;

    public boolean isJGivenFilteringEnabled() {
        return isJGivenFilteringEnabled;
    }

    public void setJGivenFilteringEnabled(boolean JGivenFilteringEnabled) {
        isJGivenFilteringEnabled = JGivenFilteringEnabled;
    }

    public static JGivenSettings getInstance() {
        return ServiceManager.getService(JGivenSettings.class);
    }
}
