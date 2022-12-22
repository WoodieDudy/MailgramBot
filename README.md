# MailgramBot

> 
<img src="assets/snippet.png" alt="snippet.png">

[![Telegram](https://badgen.net/badge/icon/telegram?icon=telegram&label=open)](https://t.me/CoolMailgramBot)
![Build status](https://github.com/WoodieDudy/MailgramBot/actions/workflows/test_and_pub.yml/badge.svg?branch=dev)

# About
> MailgramBot is an email Java-based client for Telegram that provides the functionality of receiving emails.
If you want to know more about options you can use /help.

## How to use
We have ensured that your passwords are kept safe, so you must create an application password in order to log in. See the Google [guide](https://support.google.com/accounts/answer/185833?hl=en) for details on how to do this.

You can also use another email service. We're sure that all the popular services also provide this option and have their own guide, we suggest you look it up.

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
___
## Tasks:
- [x] Implemented a conversation with the user in the console, configured the processing of incorrect commands and switching "states" (states)
   necessary to determine the currently available tools of the bot.
- [x] States replaced by command classes for each command. Added base mail functional.
- [x] Bot moved to Telegram (using Activity Bot) with buttons. Add CI/CD.
- [x] Added web-app functional for authentication (with an adaptive color scheme).