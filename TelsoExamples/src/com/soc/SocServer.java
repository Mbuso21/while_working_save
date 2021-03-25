package com.soc;

import java.io.*;
import java.net.*;

public class SocServer {

    public static void main(String[] args) throws Exception {

        System.out.println("Server: Server is started");
        ServerSocket serverSocket = new ServerSocket(9999);

        System.out.println("Server: Server is waiting for client request");
        Socket socket = serverSocket.accept();

        System.out.println("Server: Client connected");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String string = bufferedReader.readLine();

        System.out.println("Server: Client data: " + string);

        String nickName = string.substring(0, 3);

        OutputStreamWriter outputStream = new OutputStreamWriter(socket.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream);
        out.write(nickName);
        out.flush();

        System.out.println("Server: Data sent from Server to Client");


    }
}
