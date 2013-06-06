# ![CloudsdaleApp](https://secure.gravatar.com/avatar/006b4dec507eaac9967970a1cd967167?s=22) Cloudsdale for Android

Cloudsdale, on the go, on your favourite Android device

## Introduction

Cloudsdale for Android is built with *Java*, *Gradle* and *Android Studio*.

## Preparing the project

1. Pull the default branch, 0.2.0 as of this writing
2. In your terminal, issue 

```bash
git submodule init
```
3. Move to the `app/submodules/ion/ion` directory, and remove the `settings.gradle` file

You'll need to remove the settings.gradle file each time you update Ion, which should be fairly regularly

## Importing the project

1. Open Android Studio
2. Select "Import Project"
3. Browse to the root directory, and select it
4. Select the Gradle model
5. Elect to use the Gradle wrapper, and click next