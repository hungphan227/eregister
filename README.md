# where to put frontend

src/main/resources/public

# run docker

sudo docker run --name student-application -p 9998:9998 -p 9997:9997 -v /home/hung/data/docker/docker-data/log/student-application:/var/log/student-application --add-host=dbserver:192.168.204.129 --add-host=redis-server:192.168.204.129 student-application
