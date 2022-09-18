#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <fcntl.h>

#define PORT 8081
#define BUF_SIZE 4096
#define STRING_SIZE 24  

int main() {
    struct sockaddr_in clientaddr, serveraddr;
    char buffert[BUF_SIZE], Failed_response[] = "HTTP/1.1 404 not found \r\n\r\n", Successful_response[] = "HTTP/1.1 200 OK r\n\r\n", * beginning_of_string, * end_of_string, string[STRING_SIZE] = "";
    int clientaddrlen, request_sd, sd, file_opener, listen_checker = 0, bind_checker = 0, bytes = 0;

    request_sd = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
    memset(&serveraddr, 0, sizeof(struct sockaddr_in));
    serveraddr.sin_family = AF_INET;
    serveraddr.sin_addr.s_addr = INADDR_ANY;
    serveraddr.sin_port = htons(PORT);
    bind_checker = bind(request_sd, (struct sockaddr*)&serveraddr, sizeof(struct sockaddr_in));

    if (bind_checker < 0) 
    {
        perror("Bind failed!");
        }else{
        printf("Socket binded successfully!");
    }

    listen_checker = listen(request_sd, SOMAXCONN);
    if (listen_checker < 0)
    {
        perror("Listen failed!");
        }else{   
        printf("Socket listening!");
    }
    clientaddrlen = sizeof(struct sockaddr_in);

    while (1) {
        sd = accept(request_sd, (struct sockaddr*)&clientaddr, &clientaddrlen);
        memset(buffert, 0, BUF_SIZE);
        read(sd, buffert, BUF_SIZE);
        printf("%s\n", buffert);
        beginning_of_string = strchr(buffert, '/') + 1;
        end_of_string = strchr(beginning_of_string, ' ');
        strncpy(string, beginning_of_string, end_of_string - beginning_of_string);

        while (!(strcmp(string, "\0") > 1)) {
            strcpy(string, "adminview.html");
        }
        file_opener = open(string, O_RDONLY);

        if (file_opener < 0) {
            printf("Could not open the file\n");
            write(sd, Failed_response, strlen(Failed_response));
        }else {
            write(sd, Successful_response, strlen(Successful_response));
        }
        while (1) {
            bytes = read(file_opener, buffert, BUF_SIZE);
            if (bytes <= 0) {
                break;
            }
            write(sd, buffert, bytes);
        }
    }
    close(file_opener);
    close(sd);
    close(request_sd);
}