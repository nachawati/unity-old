FROM maven:3.5-jdk-8

RUN apt-get update -yq
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN apt-get update -yq
RUN apt-get install -yq nodejs

ADD . /opt/unity

RUN npm install -g /opt/unity

VOLUME "/root/.unity"
VOLUME "/workspace"
WORKDIR "/workspace"
ENTRYPOINT [ "unity" ]