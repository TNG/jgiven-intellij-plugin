Development Related Stuff
==========================

Releasing the Plugin
--------------------

* [Generate a permanent token](https://www.jetbrains.com/help/hub/Manage-Permanent-Tokens.html) via [JetBrains Hub](https://hub.jetbrains.com)
* Open your ~/.gradle/gradle.properties and enter something like \
`
jetbrainsPublishUsername=my.jetbrains.username@somedomain.com
jetbrainsPublishToken=my_newly_generated_token
`
* Update the version in `src/main/resources/META-INF/plugin.xml`
* Run `./gradlew publishPlugin`
