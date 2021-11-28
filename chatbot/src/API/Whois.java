/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Admin
 */
public class Whois {

    public static void main(String[] args) {
        System.out.println(getInfoDomain("sgu.edu.vn"));
    }

    public static String getInfoDomain(String domain) {
        // api: https://whois.inet.vn/api/whois/domainspecify/sgu.edu.vn
        String result = "";
        try {

            // lấy json về
            URL url = new URL("https://whois.inet.vn/api/whois/domainspecify/" + domain);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // dòng này fix lỗi respone code 403
            conn.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();
            //System.out.println(responseCode);
            // 200 OK
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);

                }
                in.close();

                // response là chuỗi json
                // bóc tách chuỗi json
                JSONObject obj = new JSONObject(response.toString());
                // messages là 1 mảng
                String domainName = obj.getString("domainName");
                String registrar = obj.getString("registrar");

                String nameServer = "";
                JSONArray nameServerArr = obj.getJSONArray("nameServer");
                for (int i = 0; i < nameServerArr.length(); i++) {
                    nameServer += (String) nameServerArr.get(i) + "; ";
                }
                String status = "";
                JSONArray statusArr = obj.getJSONArray("status");
                for (int i = 0; i < statusArr.length(); i++) {
                    status += (String) statusArr.get(i) + "; ";
                }

                String creationDate = obj.getString("creationDate");
                String expirationDate = obj.getString("expirationDate");
                String registrantName = obj.getString("registrantName");

                //System.out.println("Tên miền: " + domainName);
                //System.out.println("Quản lý tại nhà đăng ký: " + registrar);
                //System.out.println("Nameserver: " + nameServer);
                //System.out.println("Cờ trạng thái: " + status);
                //System.out.println("Ngày đăng ký: " + creationDate);
                //System.out.println("Ngày hết hạn: " + expirationDate);
                //System.out.println("Chủ sở hữu tên miền: " + registrantName);
                
                result = "Tên miền: " + domainName + "\n"
                        + "Quản lý tại nhà đăng ký: " + registrar + "\n"
                        + "Nameserver: " + nameServer + "\n"
                        + "Cờ trạng thái: " + status + "\n"
                        + "Ngày đăng ký: " + creationDate + "\n"
                        + "Ngày hết hạn: " + expirationDate + "\n"
                        + "Chủ sở hữu tên miền: " + registrantName;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
