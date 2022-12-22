# MailgramBot
<img src="assets/snippet.png" alt="snippet.png">

[![Telegram](https://badgen.net/badge/icon/telegram?icon=telegram&label=open)](https://t.me/CoolMailgramBot)
![Build status](https://github.com/WoodieDudy/MailgramBot/actions/workflows/test_and_pub.yml/badge.svg?branch=dev)

### MailgramBot is an email client for Telegram 

Tasks:
1. Implemented a conversation with the user in the console, configured the processing of incorrect commands and switching "states" (states)
necessary to determine the currently available tools of the bot.
2. States replaced by command classes for each command. Added base mail functional.
3. Bot moved to Telegram (using Activity Bot) with buttons. Add Ci/CD.
4. Added web-app functional for authentication (with an adaptive color scheme).

# About
Java-based Telegram bot that provides the functionality of receiving emails.
If you want to know more about options you can use /help.

## Clone the repo
```sh
$ git clone https://github.com/WoodieDudy/MailgramBot
$ cd MailgramBot
```

### Running test with maven
```sh
$ mvn clean test
```

### Running locally with maven
```sh
# Build
$ mvn -B clean package
# Run 
$ java -jar target/mailgrambot-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/config.properties
```

### Running with docker
```sh
$ docker build -t mailgrambot .
$ docker run -it mailgrambot -v /path/to/config:/app/config.properties
```