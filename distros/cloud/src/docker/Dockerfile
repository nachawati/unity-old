FROM maven:3.5-jdk-8

RUN apt-get update -yq
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN apt-get update -yq
RUN apt-get install -yq nodejs

ADD . /unity

RUN npm install -g /unity


VOLUME "/root/.dgms"
VOLUME "/root/workspace"
WORKDIR "/root/workspace"
ENTRYPOINT [ "dgms-linux-gcc-x86_64" ]