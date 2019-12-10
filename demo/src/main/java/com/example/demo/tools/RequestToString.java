package com.example.demo.tools;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestToString {
    private String req;

    public String getReq() {
        return req;
    }

    public RequestToString(HttpServletRequest request) {
        BufferedReader read = null;
        try {
            read = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if (!((line = read.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
        req = sb.toString();
    }
}
