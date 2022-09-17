#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <arpa/inet.h>


void main(int argc, char *argv){

    int  sockfd, port = 99;
    struct sockaddr_in serverAddr;
    char Buffer[1024];
    socklen_t addr_size;
    uint32_t server_time;
    time_t time;

    sockfd = socket(PF_INET, SOCK_DGRAM,0);
    memset(&serverAddr, '\0', sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);

    inet_pton(AF_INET, "127.0.0.1", &serverAddr.sin_addr);
    strcpy(Buffer, "Hello server\n");
    sendto(sockfd, Buffer, 1024, 0, (struct sockaddr*)&serverAddr, sizeof(serverAddr));
    printf ("[+]Data send: %s",Buffer);
    addr_size = sizeof(serverAddr);
    recvfrom(sockfd, &server_time, 1024, 0, (struct sockaddr*)&serverAddr, &addr_size);
    time = server_time - 2208988800;
    printf("[+]Data receieved: %s\n",ctime(&time) );

    close(sockfd);


}
