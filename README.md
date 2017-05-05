[![Build Status](https://api.travis-ci.org/TNG/jgiven-intellij-plugin.svg?branch=master)](https://travis-ci.org/TNG/jgiven-intellij-plugin)
[![License](https://img.shields.io/github/license/TNG/jgiven-intellij-plugin.svg )](https://raw.githubusercontent.com/TNG/jgiven-intellij-plugin/master/LICENSE)

JGiven Plugin for IntelliJ IDEA
===============================

[IntelliJ IDEA](https://www.jetbrains.com/idea/) plugin to support both navigating between scenario states and
 finding usages of scenario states within [JGiven](http://jgiven.org/) test stages.

Features
--------

* All scenario states with usages are annotated<br/>
![Line Marker Preview](README/lineMarker.png)

* When looking for usages of a scenario state field, IntelliJ will also show all other fields referencing this scenario state.
 To make those lists more readable, an additional usage type for JGiven scenario states is available.
 Within this list, all non scenario state usages can be filtered out by clicking the JGiven icon.<br/>
![Find Usages](README/findUsages.png)

Building
--------

Checkout all source files and run ``./gradlew buildPlugin`` from within the checkout directory.

The installable artifact can be found at ``build/distributions/jgiven-intellij-plugin.zip``.

Installation
------------

The plugin is available from within the [JetBrains plugin repository](https://plugins.jetbrains.com/plugin/9670-jgiven). Thus, either download the plugin directly
from within the IDE ("Browse repositories") or build the plugin manually and install it from your local disk.

 * Open up the ``Settings`` dialog (``File > Settings`` or hit ``Ctrl+Alt+S``).
 * Select ``Plugins`` from the tree
 * Hit ``Install plugin from disk`` and select the zip file you just created (see ``Building``).
 * Restart IntelliJ
 

![Installation](README/installation.png)

License
-------

Licensed under the Apache License, Version 2.0.