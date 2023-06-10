package com.techno71.fireservice.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {

    public  String  retirveUrl(String url) throws IOException {

        String urlData="";
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;

        try {

            URL getUrl=new URL(url);
            httpURLConnection=(HttpURLConnection)getUrl.openConnection();
            httpURLConnection.connect();

            inputStream=httpURLConnection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer=new StringBuffer();
            String line="";

            while ((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            urlData=stringBuffer.toString();
            bufferedReader.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return  urlData;
    }
}
