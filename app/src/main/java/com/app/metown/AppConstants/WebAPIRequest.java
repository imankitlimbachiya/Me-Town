package com.app.metown.AppConstants;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebAPIRequest {

    public static String PostSale(byte[] image_byt, String url1, String Image, String title, String category_id, String price,
                                  String negotiable, String description, String lats, String longs) {

        String doc = null;
        String boundary = "---------------------------14737809831466499882746641449";
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        try {
            Log.e("URL", "" + url1);
            URL url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            //connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            if (image_byt != null) {
                outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"photos[]\";filename=\"" + Image + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                Log.e("photos[]", "" + Image);
                outputStream.write(image_byt);
                outputStream.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
            }

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "title" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("title", "" + title);
            outputStream.write(title.getBytes("UTF-8"));

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "category_id" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("category_id", "" + category_id);
            outputStream.write(category_id.getBytes("UTF-8"));

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "price" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("price", "" + price);
            outputStream.write(price.getBytes("UTF-8"));

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "negotiable" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("negotiable", "" + negotiable);
            outputStream.write(negotiable.getBytes("UTF-8"));

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "description" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("description", "" + description);
            outputStream.write(description.getBytes("UTF-8"));

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "lats" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("lats", "" + lats);
            outputStream.write(lats.getBytes("UTF-8"));

            outputStream.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "longs" + "\";" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            Log.e("longs", "" + longs);
            outputStream.write(longs.getBytes("UTF-8"));

            InputStream data = connection.getInputStream();
            if (data != null) {
                StringBuilder sb = new StringBuilder();
                int b;
                while ((b = data.read()) != -1) {
                    sb.append((char) b);
                }
                data.close();
                Log.i("Image_Upload : ", sb.toString());
                return sb.toString();
            } else {
                Log.i("Image_Upload : ", "data is null");
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
