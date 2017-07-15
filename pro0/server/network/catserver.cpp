#include <stdio.h>
#include <fstream>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <algorithm>

using namespace std;

int main(int argc, char* argv[]){
    int sockfd, newsockfd, portno;
    socklen_t clilen;
    struct sockaddr_in serv_addr, cli_addr;
    char buffer[256];
    string line;
    ifstream f(argv[1]);
    if (!f.is_open()){
        printf("File %s not exists", argv[1]);
        exit(1);
    }
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    portno = atoi(argv[2]);
    bzero((char*) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_port = htons(portno);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    if (bind(sockfd, (struct sockaddr*) &serv_addr, sizeof(serv_addr)) < 0){
        printf("Error in binding");
        exit(1);
    }
    listen(sockfd, 5);
    clilen = sizeof(cli_addr);
    newsockfd = accept(sockfd, (struct sockaddr*) &cli_addr, &clilen);
    if (newsockfd < 0){
        printf("Error in accept");
        exit(1);
    }
    bzero(buffer, 256);
    while(read(newsockfd, buffer, 255) >= 0){
        if (strcmp(buffer, "LINE\n") == 0){
            if (!getline(f, line)){
                f.clear();
                f.seekg(0, ios_base::beg);
                getline(f, line);
            }
            transform(line.begin(), line.end(), line.begin(), ::toupper);
            //printf("server send %s\n", line.c_str());
            if (write(newsockfd, line.c_str(), line.length()) < 0){
                printf("Error in writing socket");
                exit(1);
            }
        }
    }
    close (newsockfd);
    close (sockfd);
    return 0;
}