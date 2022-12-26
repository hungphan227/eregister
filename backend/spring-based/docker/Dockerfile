FROM openjdk:8
ADD eregister-1.0.0.jar eregister-1.0.0.jar
# RUN apt-get install vim
# RUN apt-get install telnet
RUN mkdir -p /var/log/eregister && touch /var/log/eregister/eregister.log
CMD java -jar eregister-1.0.0.jar > /var/log/eregister/eregister.log 2>&1
# CMD java -jar eregister-1.0.0.jar
