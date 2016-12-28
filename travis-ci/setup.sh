#!/usr/bin/env sh
sudo apt-get -qq update
wget https://dl.google.com/android/repository/android-ndk-r13b-linux-x86_64.zip
tar xf android-ndk-r13b-linux-x86_64.tar.bz2
mv android-ndk-r13b android-ndk