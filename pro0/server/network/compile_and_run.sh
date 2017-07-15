dir=$1
data=$2
portno=$3


g++ $dir/catserver.cpp -o $dir/catserver
$dir/catserver $data $portno