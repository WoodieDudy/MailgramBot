# MailgramBot
<img src="assets/snippet.png">

[![GitHub](https://badgen.net/badge/icon/telegram?icon=telegram&label=open)](https://t.me/CoolMailgramBot)
![Build status](https://github.com/WoodieDudy/MailgramBot/actions/workflows/maven.yml/badge.svg?branch=cicd)

### MailgramBot is a email client for Telegram 

Tasks:
1. Implemented a conversation with the user in the console, configured the processing of incorrect commands and switching "states" (states)
necessary to determine the currently available tools of the bot.

# About.
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
$ mvn clean compile exec:java
```

### Running with docker
```sh
$ docker build -t mailgrambot .
$ docker run -it mailgrambot
```