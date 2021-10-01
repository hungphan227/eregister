FROM openjdk:8
ADD student-application-1.0.0.jar student-application-1.0.0.jar
# RUN apt-get install vim
# RUN apt-get install telnet
RUN mkdir -p /var/log/student-application && touch /var/log/student-application/student-application.log
CMD java -jar student-application-1.0.0.jar > /var/log/student-application/student-application.log 2>&1
