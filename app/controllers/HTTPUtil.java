package controllers;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HTTPUtil {

    public static String connectAndGetResponse(String urlStr,String method) throws Exception
    {
        URL url;
        HttpURLConnection connection = null;
        StringBuffer response = new StringBuffer();
        try {
            //Create connection
            url = new URL(urlStr);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
//                connection.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded");
//                connection.setRequestProperty("Content-Length", "" +
//                        Integer.toString(urlParams.getBytes().length));
//                connection.setRequestProperty("Content-Language", "en-US");
//                //Send request
//                DataOutputStream wr = new DataOutputStream (
//                        connection.getOutputStream ());
//                wr.writeBytes (urlParams);
//                wr.flush ();
//                wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;

            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();


        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }
}
