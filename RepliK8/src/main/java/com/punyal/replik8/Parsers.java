/*
 * The MIT License
 *
 * Copyright 2015 Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.punyal.replik8;

import com.punyal.replik8.resource.ResourceInfo;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class Parsers {
    private static final Logger log = Logger.getLogger(Parsers.class.getName());
    
    static public String getHostFromURI(String uri) {
        String host = null;
        try {
            URL url = new URL("http"+uri.substring(4));
            host = url.getHost();
        } catch (MalformedURLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return host;
    }
    
    static public int getPortFromURI(String uri) {
        int port = -1;
        try {
            URL url = new URL("http"+uri.substring(4));
            port = url.getPort();
        } catch (MalformedURLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        if (port == -1) port = Constants.DEFAULT_COAP_PORT;
        return port;
    }
    
    static public List<ResourceInfo> parseWellKnownCore(String response) {
        List<String> textList = Arrays.asList(response.split(","));
        List<ResourceInfo> resourcesList = new ArrayList<>();
        for (String info : textList) {
            resourcesList.add(parseResourceInfo(info));
        }
        return resourcesList;
    }
    
    static public ResourceInfo parseResourceInfo(String response) {
        List<String> infoList = Arrays.asList(response.split(";"));
        String title = null;
        String path = null;
        String att_ct = null;
        String att_rt = null;
        String att_sz = null;
        String att_if = null;
        boolean obs = false;
        
        for (String info : infoList) {
            if (info.substring(0, 1).equals("<")) { // path detection
                path = info.substring(1,info.lastIndexOf(">"));
            } else if(info.equals("obs")) { // Observable resource
                obs = true;
            } else {
                String[] att = info.split("=");
                switch(att[0]) {
                    case "title":
                        title = att[1].substring(1, att[1].length()-1);
                        break;
                    case "ct":
                        att_ct = att[1];
                        break;
                    case "rt":
                        att_rt = att[1];
                        break;
                    case "sz":
                        att_sz = att[1];
                        break;
                    case "if":
                        att_if = att[1];
                        break;
                    default:
                        log.log(Level.WARNING, "UNKNOWN ATTRIBUTE: {0}", att);
                        break;
                }
            }
        }
        return new ResourceInfo(path, title, att_ct, att_rt, att_sz, att_if, obs);
    }
    
    static public String generateURI(InetAddress address, int port, String resource) {
        String uri;
        if (address instanceof Inet6Address)
            uri = "coap://["+address.getHostAddress()+"]:"+port+resource;
        else
            uri = "coap://"+address.getHostAddress()+":"+port+resource;
        return uri;
    }
    
    static public String byte2string(byte[] bytes) {
        return new String(bytes);
    }
}
