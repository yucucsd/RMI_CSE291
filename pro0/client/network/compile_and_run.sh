dir=$1
data=$2
server_ip=$3
portno=$4


g++ $dir/catclient.cpp -o $dir/catclient
$dir/catclient $data $portno $server_ip