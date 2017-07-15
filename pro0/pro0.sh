echo "Building images"
#build images(client, server, data volume)
docker build -t yuc/ubuntu_client $(pwd)/client
docker build -t yuc/ubuntu_server $(pwd)/server
docker build -t yuc/ubuntu_data $(pwd)/data
echo "Start running"
#run container
docker run -d --name data yuc/ubuntu_data
docker run -itd --name server -v $(pwd)/server/network:/root/network --volumes-from data yuc/ubuntu_server bash /root/network/compile_and_run.sh /root/network /data/string.txt 5000
server_ip=$(docker inspect --format '{{.NetworkSettings.IPAddress}}' server)
docker run -itd --name client -v $(pwd)/client/network:/root/network --volumes-from data yuc/ubuntu_client bash /root/network/compile_and_run.sh /root/network /data/string.txt $server_ip 5000
for i in {1..10}; do
    echo "Updated result: "
    docker logs client
    #docker logs server
    sleep 3
done

docker stop data client server
docker rm data client server
