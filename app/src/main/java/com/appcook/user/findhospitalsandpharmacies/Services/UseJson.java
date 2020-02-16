package com.appcook.user.findhospitalsandpharmacies.Services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownServiceException;

public class UseJson {

    private String url;

    public UseJson() {
        url = null;
    }

    public UseJson(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UseJson{" +
                "url=" + url +
                '}';
    }

    public String useJsonString(String parameters) throws Exception {
        HttpURLConnection con;
        DataOutputStream wr;
        BufferedReader in;
        try {
            con = (HttpURLConnection) new URL(this.url).openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.addRequestProperty("Cache-Control", "no-cache");
            con.addRequestProperty("Cache-Control", "max-age=0");
            con.setRequestMethod("POST");
            con.setDoOutput(false);
            con.setConnectTimeout(10000);
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
            String results = response.toString();
            return results;

        } catch (NullPointerException e) {
            con = null;
            return "";
        } catch (ProtocolException e) {
            con = null;
            return "";
        } catch (SecurityException e) {
            con = null;
            return "";
        } catch (IllegalStateException e) {
            con = null;
            return "";
        } catch (UnknownServiceException e) {
            con = null;
            wr = null;
            return "";
        } catch (IOException e) {
            con = null;
            wr = null;
            in = null;
            return "";
        } catch (Exception e) {
            con = null;
            wr = null;
            in = null;
            return "";
        }
    }
}
