package com.allianz.api.beans;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.myfaces.trinidad.model.UploadedFile;

public class FileUploadClient {
    public static String getFileExtn(String filename) {
        String parts[] = filename.split("\\.(?=[^\\.]+$)");
        return parts[1].toLowerCase();
    }
    
    public static File uploadedFileToFileConverter(UploadedFile uf) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        //Add you expected file encoding here:
        System.setProperty("file.encoding", "UTF-8");
        File newFile = new File(uf.getFilename());
        try {
            inputStream = uf.getInputStream();
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
           //Do something with the Exception (logging, etc.)
        }
        return newFile;
    }

    public static void fileUpload(String url, File file) {
        CloseableHttpClient httpclient = null;
        try {
            //Enter your host and port number...
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "multipart/form-data; boundary=\"----WebKitFormBoundary7MA4YWxkTrZu0gW\"");
            //path of local file and correct for rest of the files also

            String message = "This is a multipart post";
            // Create Multipart instance
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // Add image file to upload
            builder.addBinaryBody("file", file, ContentType.create("multipart/form-data"), file.getName());
            builder.setBoundary("----WebKitFormBoundary7MA4YWxkTrZu0gW");

            httpclient = HttpClientBuilder.create().build();
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            // execute the post request
            HttpResponse response = httpclient.execute(post);
            // Read the response HTML
            if (response != null) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // Read the response string if required
                    InputStream responseStream = responseEntity.getContent();
                    if (responseStream != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
                        String responseLine = br.readLine();
                        String tempResponseString = "";
                        while (responseLine != null) {
                            tempResponseString =
                                tempResponseString + responseLine + System.getProperty("line.separator");
                            responseLine = br.readLine();
                        }
                        br.close();
                        if (tempResponseString.length() > 0) {
                            System.out.println(tempResponseString);
                        }
                    }
                    responseStream.close();
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}
