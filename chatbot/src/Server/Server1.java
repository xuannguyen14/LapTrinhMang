/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import API.Simsimi;
import API.Whois;
import API.currencyConverter;
import API.thoiTiet;
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
    private static BufferedWriter out = null;

    public static String huongDanCuPhap() {
        return " Cú pháp xem thời tiết: 'thoitiet;' + 'tên thành phố' \t vd: thoitiet;london \t vd:thoitiet;ho+chi+minh (nếu tên thành phố có 2 chữ cái trở lên thì thêm dấu '+') \t " 
                + "Cú pháp xem thông tin domain: 'whois; + 'tên miền' \t vd: whois;sgu.edu.vn";
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(5100);
            while (true) {
                try {
                    System.out.println("Server started, waiting for clients...");
                    socket = server.accept();
                    System.out.println("Client " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " is connected");
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    while (true) {
                        String line = in.readLine();

                        if (line.equals("bye")) {

                            break;
                        }
                        System.out.println("Server received " + line);

                        // chỗ nãy để xử lý chức năng
                        // nếu chuỗi chứa từ currencyConverter thì sẽ thực hiện chức năng chuyển tiền
                        // line sẽ có dạng: currencyConverter USD VND 1000  sau đó dùng StringTokenizer tách ra
                        if (line.contains("currencyConverter")) {
                            //  line = in2.readLine();
                            System.out.println(line);
                            StringTokenizer st = new StringTokenizer(line, " ");
                            while (st.hasMoreTokens()) {
                                String syntax = st.nextToken(); // syntax là cờ hiệu currencyConverter nên không lấy
                                String chuoi1 = st.nextToken();
                                String chuoi2 = st.nextToken();
                                String chuoi3 = st.nextToken();
                                line = currencyConverter.convertMoney(chuoi1, chuoi2, chuoi3);
                            }

                        } else if (line.contains("thoitiet;")) { // nếu trong chuỗi có chứa chữ thoitiet thì thực hiện chức năng xem thời tiết
                            StringTokenizer st = new StringTokenizer(line, ";");

                            while (st.hasMoreTokens()) {
                                String syntax = st.nextToken(); // syntax là cờ hiệu nên không lấy
                                String city = st.nextToken();   // token thứ 2 là tên thành phố
                                line = thoiTiet.getWeather(city); // truyền tên thành phố vào hàm getWeather
                                System.out.println(line);
                            }

                        } else if (line.contains("whois;")) {
                            StringTokenizer st = new StringTokenizer(line, ";");
                            while (st.hasMoreTokens()) {
                                String syntax = st.nextToken(); // syntax là cờ hiệu nên không lấy
                                String domain = st.nextToken();   // token thứ 2 là tên domain
                                line = Whois.getInfoDomain(domain); // truyền tên domain vào hàm getWeather
                                System.out.println(line);
                            }
                            
                        } else if (line.equals("cuphap")) {
                            line = huongDanCuPhap();
                        } else {
                            line = Simsimi.getResponeFromSimsimi(line);
                        }

                        out.write(line);
                        out.newLine();
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
