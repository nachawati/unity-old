FROM maven:3.5-jdk-8

RUN apt-get update -yq

RUN apt-get install -yq apt-transport-https
RUN apt-get install -yq ca-certificates
RUN apt-get install -yq curl
RUN apt-get install -yq gnupg2
RUN apt-get install -yq software-properties-common
RUN apt-get install -yq build-essential 
RUN apt-get install -yq cmake

RUN apt-get install -yq sudo
RUN useradd -m docker && echo "docker:docker" | chpasswd && adduser docker sudo

RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | sudo apt-key add -
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") $(lsb_release -cs) stable"
RUN apt-get update -yq
RUN apt-get install -yq docker-ce

RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN apt-get update -yq

RUN apt-get install -yq nodejs