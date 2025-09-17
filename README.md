[curse-downloads-shield]: https://img.shields.io/curseforge/dt/535416
[curse-files-url]: https://www.curseforge.com/minecraft/mc-mods/gigeresque/files

[modrinth-downloads-shield]: https://img.shields.io/modrinth/dt/2kXK3iBI
[modrinth-files-url]: https://modrinth.com/mod/gigeresque/versions/files

[discord-shield]: https://dcbadge.limes.pink/api/server/https://discord.gg/4VnZ2CeKX4
[discord-url]: https://discord.gg/4VnZ2CeKX4

[IntelliJ IDEA]: https://www.jetbrains.com/idea/
[Gradle]: https://www.gradle.org/
[Java Development Kit 21]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[CurseForge]: https://preview.redd.it/i-heard-that-curseforge-rebranded-their-logo-is-it-good-or-v0-hualodaes7ka1.png?auto=webp&s=985d21b6461a7e0be2dfb5d4e3beb64401711c9e

<br><br><br>
<center>
<img src="https://cdn.modrinth.com/data/cached_images/f9290aafce04fe0e00365df3bd62e7b5a8b0dac6_0.webp" alt="Gigeresque"/>
<br><br>
<a href="https://modrinth.com/mod/azurelib"> <img src="https://azuredoom.com/cozy_64h.png" alt="logo" height="64"  width="170" /> </a>
<a href="https://modrinth.com/mod/fabric-api"> <img src="https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/requires/fabric-api_64h.png?raw=true" alt="logo" height="64"  width="186"/> </a>
<br><br>
<a href="https://neoforged.net/"> <img src="https://i.imgur.com/TGnV6jv.png" alt="logo" height="64"  width="186"/> </a>
<a href="https://fabricmc.net/"> <img src="https://github.com/intergrav/devins-badges/blob/v3/assets/cozy/supported/fabric_64h.png?raw=true" alt="logo" height="64"  width="170" /> </a>


=============

[![discord-shield]][discord-url]

<img src="https://i.imgur.com/L6vA521.png" alt="CurseForge" width="200"/>
<img src="https://img.shields.io/curseforge/dt/535416" alt="CurseForge" width="200"/>
<br>
<img src="https://crowdin-static.downloads.crowdin.com/images/project-logo/518556/small/d0d0fa84ec9d7863f8ce01c6a4352272374.png" alt="Modrinth" width="200"/>
<img src="https://img.shields.io/modrinth/dt/2kXK3iBI" alt="Modrinth" width="200"/>

</center>

## Prerequisites ##
* [Java Development Kit 21]
* [Gradle]

## Contributing ##
Before contributing to this project, please ensure that you read the following guidelines. It's crucial to have your development environment configured correctly. Additionally, pull requests that do not adhere to the specified format will not be accepted.

### 1) Clone The Repository ###
Follow the steps here: https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository

### 2) Setup in [IntelliJ IDEA] ###
1. Open the template's root folder as a new project in IDEA. This is the folder that contains this README.md file and the gradlew executable.
2. If your default JVM/JDK is not Java 21 you will encounter an error when opening the project. This error is fixed by going to `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM` and changing the value to a valid Java 21 JVM. You will also need to set the Project SDK to Java 21. This can be done by going to `File > Project Structure > Project SDK`. Once both have been set open the Gradle tab in IDEA and click the refresh button to reload the project.
3. Open your Run/Debug Configurations. Under the `fabric` category there will be an option to run `runDatagen`, you'll want to run this before trying to start the game in step 4.
4. Under the `Application` category there should now be options to run Fabric and NeoForge projects. Select one of the client options and try to run it.
5. Assuming you were able to run the game in step 4 your workspace should now be set up.

__**Eclipse/VSCode not supported**__

### 3) Creating a PR ###
Please do at least one build via `Tasks > build > build` to have the spotless plugin format all code before submitting the PR.

Follow the steps here: https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request
