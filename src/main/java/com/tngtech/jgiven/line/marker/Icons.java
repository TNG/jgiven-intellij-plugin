package com.tngtech.jgiven.line.marker;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

class Icons {
    private static final String JGIVEN_PNG = "/icons/jgiven.png";

    static final Icon JGIVEN = load(JGIVEN_PNG);

    private static Icon load(String path) {
        return IconLoader.getIcon(path, Icons.class);
    }
}
