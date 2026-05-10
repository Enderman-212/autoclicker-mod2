# AutoClicker Fabric Mod for Minecraft 1.21.1

## How to Build

### Requirements
- Java 21 JDK (not just JRE)
- Internet connection (to download Gradle and Fabric dependencies)

### Steps
1. Extract this folder
2. Open a terminal inside the `autoclicker-mod/` folder
3. Run:
   - **Windows:** `gradlew.bat build`
   - **Mac/Linux:** `./gradlew build`
4. Find the compiled JAR at: `build/libs/autoclicker-1.0.0.jar`
5. Copy that JAR into your Minecraft `.minecraft/mods/` folder

> Make sure you have Fabric Loader 0.16.5+ and Fabric API installed.

## How It Works
- Press **CapsLock** once → AutoClicker turns ON (20 CPS)
- Press **CapsLock** again → AutoClicker turns OFF
- A status message appears on your HUD
- Clicking is blocked when any GUI/inventory is open
