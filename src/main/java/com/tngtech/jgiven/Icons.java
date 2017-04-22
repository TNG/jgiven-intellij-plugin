package com.tngtech.jgiven;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class Icons {
    private static final String JGIVEN_PNG = "/icons/jgiven.png";

    public static final Icon JGIVEN = load(JGIVEN_PNG);

    private static Icon load(String path) {
        return IconLoader.getIcon(path, Icons.class);
    }
}
