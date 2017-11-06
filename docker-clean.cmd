@ECHO OFF

SETLOCAL

SET basedir=%~dp0
IF %basedir:~-1%==\ SET basedir=%basedir:~0,-1%
SET basedir=%basedir:\=/%
SET basedir=%basedir:~1%
SET basedir=/c%basedir::=%

SET homedir=%userprofile%
IF %homedir:~-1%==\ SET homedir=%homedir:~0,-1%
SET homedir=%homedir:\=/%
SET homedir=%homedir:~1%
SET homedir=/c%homedir::=%

docker build -t unity-build:latest %~dp0src\docker
docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v "%basedir%:/build" -v "%homedir%/.m2:/root/.m2" -w /build unity-build:latest mvn clean