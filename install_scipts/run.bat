echo off

set JAVAFX_DOWNLOAD_URL=https://download2.gluonhq.com/openjfx/19/openjfx-19_windows-x64_bin-sdk.zip
set JAVAFX_LIB_PATH=.\javafx-sdk-19\lib\

echo !!!
echo !!!
echo requires java version 19+
echo if any problems occur (e.g. white screen), try to run with options: debug nosound
echo !!!
echo !!!

if not exist %JAVAFX_LIB_PATH% (
    echo lib dir not found
    curl.exe --output lib.zip --url %JAVAFX_DOWNLOAD_URL%
    tar.exe -xf lib.zip
) else (
    echo lib dir found
)

java.exe -Dprism\dirtyopts=false --module-path %JAVAFX_LIB_PATH% --add-modules javafx.controls,javafx.fxml,javafx.media -jar gruppe07.jar %* debug