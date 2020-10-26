# BasicMsg [1.2.1]
- Created in 17/10/20


## Introduction:

- This add-on is about messaging, with advanced options and customizable messages. The index of the plugin is:

1. Options
2. API
3. Commands
4. Permissions
5. Sounds

## 1. Options:

Messages:

- You can message with others, and you can toggle pm.
- You can reply with other players, see who sended you a message, and you can recevie if that player left the game.
- You can ignore other players, and see the list.
- You can use the socialspy and see who uses it.

Utils:
- You can put sound in the commands.

Requeriments:
- Compatible with PlaceholderAPI.

## 2. API:
- You can use maven:

Step 1. Put the repositories.
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
 ```
Step 2. Add the dependencies.
```xml
<dependency>
  <groupId>com.github.turbodavid999</groupId>
  <artifactId>BasicMsg</artifactId>
  <version>1.3</version>
  <scope>provided</scope>
</dependency>
```
## 3. Commands:

- /msg [player] [message]: To send a message to other player.
> -toggle [player]: To toggle your PM.

- /reply [message]: To reply a player.
> -player: To see who talked with you.

- /ignore [player] [message]: To ignore a player-
> -list: To see who you ignored.

- /socialspy [on] [player]: To toggle the spymode
> -list: To see who you use spy mode.

- /bmsg [args]: The center command,

## 4. Perms:

Note: You can modify the perms in the config.yml.
- bmsg.color: To send a private message with colors.
- bmsg.reload: To reload the plugin.
- bmsg.socialspy: To toggle the socialspy.
- bmsg.mtoggle.admin: To toggle your private messages.

## 5. Sounds:
- You can put the sounds:
- Link: https://www.spigotmc.org/wiki/cc-sounds-list/

Note: If your server is 1.9+, the mode will be disabled [because of the SOUND ID, to avoid errors].
Obiously, you can activate again.

Thanks for downloading the plugin <3.
