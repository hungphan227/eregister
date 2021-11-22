# where to put frontend

src/main/resources/public

# run docker

sudo docker run --name eregister -p 9998:9998 -p 9997:9997 -v /home/hung/data/docker/docker-data/log/eregister:/var/log/eregister --add-host=dbserver:192.168.204.129 --add-host=redis-server:192.168.204.129 eregister
