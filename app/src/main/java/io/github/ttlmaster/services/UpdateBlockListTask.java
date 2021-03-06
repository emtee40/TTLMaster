package io.github.ttlmaster.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import io.github.ttlmaster.TtlApplication;

public class UpdateBlockListTask extends Task<String,Set<String>> {

    Set<String> action(String p) {

        Set<String> out = new HashSet<>();
        URL url;

        try {
            url = new URL(p);
        } catch (MalformedURLException e) {
            TtlApplication.loge(e.toString());
            setException(e);
            return null;
        }

        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()));

            String inputLine;

            try {
                while ((inputLine = in.readLine()) != null) {
                    if (!inputLine.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+")) {
                        try {
                            InetAddress[] addresses = InetAddress.getAllByName(inputLine);
                            for (InetAddress addr : addresses) {
                                out.add(addr.getHostAddress());
                                TtlApplication.logi(String.format("%s -> %s", inputLine, addr.getHostAddress()));
                            }
                        } catch (UnknownHostException e) {
                            TtlApplication.logi(e.toString());
                        }
                    }else {
                        TtlApplication.logi(String.format("Adding usual addr %s", inputLine));
                        out.add(inputLine);
                    }
                }
            } catch (IOException e) {
                TtlApplication.loge(e.toString());
                setException(e);
            } finally {
                in.close();
            }

        } catch (IOException e) {
            TtlApplication.logi(e.toString());
            setException(e);
            return null;

        }

        return out;
    }
}
