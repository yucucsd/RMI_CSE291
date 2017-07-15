#include<stdio.h>
#include<fstream>
#include<unistd.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<netdb.h>
#include<string.h>
#include<stdlib.h>
#include<algorithm>

#define period 30
#define gap 3

using namespace std;

int main(int argc, char* argv[]){
    int sockfd, portno;
    struct hostent* server;
    struct sockaddr_in serv_addr;
    char buffer[256] = "LINE\n";
    char rec_buffer[256];
    string line;
    ifstream f(argv[1]);
    if (!f.is_open()){
        printf("File %s not exists", argv[1]);
        exit(1);
    }
    portno= atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    server = gethostbyname(argv[3]);
    bzero((char*) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char*)server->h_addr, (char*)&serv_addr.sin_addr.s_addr, server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd, (struct sockaddr*) &serv_addr, sizeof(serv_addr)) < 0){
        printf("Cannot connect to server");
        exit(0);
    }
    for (int i = 0; i < period; i += gap){
        bool exist = false;
        if (write(sockfd, buffer, strlen(buffer)) < 0){
            printf("Cannot write to socket");
            exit(0);
        }
        //printf("Buffer sent: %s\n", buffer);
        bzero(rec_buffer, 256);
        if(read(sockfd, rec_buffer, 255) < 0){
            printf("Cannot read from socket");
            exit(0);
        }
        //printf ("Receive: %s\n", rec_buffer);
        f.clear();
        f.seekg(0, ios_base::beg);
        while (getline(f, line)){
            transform(line.begin(), line.end(), line.begin(), ::toupper);
            if (strcmp(line.c_str(), rec_buffer) == 0){
                printf("OK\n");
                exist = true;
                break;
            }
        }
        if (!exist){
            printf("Missing\n");
        }
        usleep(gap * 1000000);
    }
    close(sockfd);
    return 0;
}