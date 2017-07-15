#stop and remove all existing containers first
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)


#create the server container
cd server
docker build -t myserver .
docker run -i -t -d --name myserver myserver /bin/bash -c " cd src && javac myserver.java && javac ./rmi/*.java && java myserver 8888"
#create the server and client images
cd ..
#create and run the client
serverhost=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' myserver)
cd client
docker build -t myclient .
docker run  -i -t -d --name myclient myclient /bin/bash -c " cd src && javac myclient.java && javac ./rmi/*.java && java myclient $serverhost 8888 3"
docker logs -f myclient

