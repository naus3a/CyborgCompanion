[<-back-](reasme.md)
# Stateless Password Manager

## What it is
A stateless password manager generates passwords on demand instead of storing/encrypting/decrypting your passowrds like traditional password managers.

The idea is that you eliminate some of the annoying parts of password keeping; i.e. you get rid of:
* the need to find a safe and always accessible place to store your passwords
* the need of backups
* the need for a master password to encrypt/decrypt your vault

It's an idea cryptography people have been discussing for quite a while; google it, if you want to learn more.

## How you use it
Super easy: let's say you want to generate a password for your email: you punch `youremail@yourprovider.com` into the `account` field, you scan your implant and you get your password.

You want a password for your account at `randomwebsite.com`? Just type `yourusername@randomwebsite.com`, or any other string that contains references to your uaer and the service you're trying to access. You get the idea.

## How it works
I use [ykdroid](https://github.com/pp3345/ykDroid) to interact with the `HMAC-SHA1` applet on my [apex](https://vivokey.com/apex/) implant: 
* first the applet takes the account string, adds a unique [salt](https://en.m.wikipedia.org/wiki/Salt_(cryptography)) (that is generated inside the implant itself and can never leave the hardware) and computes an [hash](https://en.m.wikipedia.org/wiki/HMAC)
* then the app renders the computed hash as a string of the desired length, making sure it contains at least 1 loweecase character, 1 uppercase charactwr, 1 digit, 1 symbol.
