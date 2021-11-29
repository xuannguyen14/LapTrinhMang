/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import API.Simsimi;
import API.currencyConverter;
import Clients.CurrencyConverterForm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Admin
 */
public class Server1 {

    private static ServerSocket server = null;
    private static Socket socket = null;
    private static BufferedReader in = null;
    private static BufferedReader in2 = null;
    private static BufferedWriter out = null;
    private static BufferedWriter out2 = null;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(5100);
            while (true) {
                try {
                    System.out.println("Server started, waiting for clients...");
                    socket = server.accept();
                    System.out.println("Client " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " is connected");
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    while (true) {
                        String line = in.readLine();

                        if (line.equals("bye")){

                            break;
                        }
                        System.out.println("Server received " + line);

                        // chỗ nãy để xử lý chức năng
                        if (line.equals("chuyendoitien")) {
                          //  line = in2.readLine();
                            System.out.println(line);
                            StringTokenizer st = new StringTokenizer(line, " ");
                            while (st.hasMoreTokens()) {
                                String chuoi1 = st.nextToken();
                                String chuoi2 = st.nextToken();
                                String chuoi3 = st.nextToken();
                               line = currencyConverter.convertMoney(chuoi1, chuoi2, chuoi3);
                            }
                            
                        } else {
                            line = Simsimi.getResponeFromSimsimi(line);
                        }

                        out.write(line + "\n");
                        out.flush();

                    }

                    System.out.println("Server closed connection");
                    in.close();
                    out.close();
                    socket.close();

                } catch (IOException e) {
                    System.err.println("Lỗi ở đâu đó trong vòng lặp" + e);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
