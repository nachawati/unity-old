FROM jetty:9-jre8

USER root

RUN apt-get update -yq
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN apt-get update -yq
RUN apt-get install -yq nodejs

# USER root
#RUN apk add --update nodejs
ADD . /opt/unity
ADD ./server /var/lib/jetty/webapps/ROOT
RUN npm install -g /opt/unity

USER jetty

EXPOSE 8080