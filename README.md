# AntiCrypt-Vanilla
Removes Mojang's message signing system from a Vanilla server, without modifying any other gameplay components.

# Usage
Download the latest jar from the releases on the left, and put it into the same folder as your **vanilla** server (this will not work on any sort of modded server).

Modify your start command to add `-javaagent:anticrypt.jar` at the start, like follows:
```
java -javaagent:anticrypt.jar -Xmx2G -jar server.jar
```
If all is working, you should see something similar to the following output in your console:
```
[AntiCrypt] Server version is 1.19 Pre-release 2 (1.19-pre2)
[AntiCrypt] Looking for akq$b
Starting net.minecraft.server.Main
[AntiCrypt] Found akq$b
[AntiCrypt] Class akq$b patched!
```
To make sure it works, join the server and send a message with your game log open. There should be a warning about an invalid signature.

# Compatability

Theoretically this should work with any version of Minecraft, even snapshots. It works by using mappings to locate the proper class, and then patches it with a generic method to zero out the chat signatures.
