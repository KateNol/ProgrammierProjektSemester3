#!/bin/bash

JAVAFX_DOWNLOAD_URL=https://download2.gluonhq.com/openjfx/19/openjfx-19_linux-x64_bin-sdk.zip
JAVAFX_LIB_PATH="lib/javafx-sdk-19/lib"

echo -e "!!!\n!!!"
echo "requires java version 19+"
echo -e "if any problems occur (e.g. white screen), try to run with options: debug nosound\n!!!\n!!!\n!!!"

if [ ! -d "$JAVAFX_LIB_PATH" ]; then
  # Take action if $DIR exists. #
  echo "lib dir not found"
  wget -O javafx.zip $JAVAFX_DOWNLOAD_URL
  unzip javafx.zip -d lib

else
  echo "lib dir found"
fi

java -Dprism.dirtyopts=false --module-path $JAVAFX_LIB_PATH --add-modules javafx.controls,javafx.fxml,javafx.media -Dfile.encoding=US-ASCII -Dsun.stdout.encoding=US-ASCII -Dsun.stderr.encoding=US-ASCII -jar gruppe07.jar "$@" debug
