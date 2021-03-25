package com.soc;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocClient {

    public static void main(String[] args) throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        String ip = "localhost";
        /*
        0 - 1023 – the Well Known Ports, also referred to as System Ports.
        1024 - 49151 – the Registered Ports, also known as User Ports.
        49152 - 65535 – the Dynamic Ports, also referred to as the Private Ports.
        *
         */
        int port = 9999;//port number ranges from 0-1023 to 65535
        Socket socket = new Socket(ip, port);
        String str = scanner.nextLine();

        // We are writing data to the server
        // We send data using the belowe
        OutputStreamWriter outputStream = new OutputStreamWriter(socket.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream);
        out.println(str);
        outputStream.flush();

        // We receive data using the below
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String nickName = bufferedReader.readLine();

        System.out.println("Client: Data from server: " + nickName);

    }
}
