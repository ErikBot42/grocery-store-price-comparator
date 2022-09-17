#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <stdint.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>


void main(int argc, char *argv){

        int sockfd, s_time, port = 99;       
        struct sockaddr_in si_me, si_other;
        char buffer[1024];
        time_t buf;
        socklen_t addr_size;                

        sockfd = socket(AF_INET, SOCK_DGRAM, 0);
        memset(&si_me, '\0', sizeof(si_me));
        si_me.sin_family=AF_INET;
        si_me.sin_port=htons(port);
        si_me.sin_addr.s_addr = INADDR_ANY;

        bind(sockfd, (struct sockaddr*)&si_me, sizeof(si_me));
        printf("[+]socket bound to server adress\n");
        addr_size = sizeof(si_other);
        while(1){

            recvfrom(sockfd, buffer, 1024, 0, (struct sockaddr*)&si_other, &addr_size);
            printf("[+]Data received %s", buffer);
            s_time = time(NULL) + 2208988800;
            s_time= htonl(s_time);
            sendto(sockfd, &s_time, sizeof(s_time), 0, (struct sockaddr*)&si_other, addr_size);
            printf("[+] Data send: %u\n\n",s_time);}

        
    close(sockfd);
}
