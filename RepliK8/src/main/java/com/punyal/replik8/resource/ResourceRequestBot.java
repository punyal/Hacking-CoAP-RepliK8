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
package com.punyal.replik8.resource;

import com.punyal.replik8.Configuration;
import com.punyal.replik8.Constants;
import com.punyal.replik8.Constants.CoapMethod;
import com.punyal.replik8.Parsers;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class ResourceRequestBot extends Thread {
    private static final Logger log = Logger.getLogger(ResourceRequestBot.class.getName());
    private final Configuration configuration;
    private final ResourceInfo info;
    private final CoapMethod method;
    private boolean running = false;
    
    private ResponseCode responseCode;
    private byte[] payload;
    private int contentFormat;
    
    public ResourceRequestBot(Configuration configuration, ResourceInfo info, CoapMethod method) {
        this.configuration = configuration;
        this.info = info;
        this.method = method;
        this.setDaemon(true);
    }
    
    public void startBot() {
        running = true;
        start();
    }
    
    public void stopBot() {
        running = false;
    }
    
    public ResponseCode getResponseCode() {
        return responseCode;
    }
    
    public byte[] getPayload() {
        return payload;
    }
    
    public int getContentFormat() {
        return contentFormat;
    }
    
    @Override
    public void run() {
        CoapClient coapClient;
        CoapResponse response = null;
        String uri;
        try {
            while (running) {
                coapClient = new CoapClient(Parsers.generateURI(configuration.getRemoteAddress(), configuration.getRemotePort(), info.getPath()));
                
                switch (method) {
                    case GET:
                        response = coapClient.get();
                        break;
                    case PUT: // TODO: implement
                        break;
                    case POST: // TODO: implement
                        break;
                    case DELETE: // TODO: implement
                        break;
                    default:
                        running = false;
                        break;
                }
                
                if (response != null) {
                    responseCode = response.getCode();
                    payload = response.getPayload();
                    contentFormat = response.getOptions().getContentFormat();
                } else {
                    log.log(Level.FINE, "Resource <"+info.getPath()+"> no response for method "+method);
                }
                
                try {
                    Thread.sleep(Constants.DEFAULT_REQUEST_TIMEOUT);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            log.log(Level.WARNING, "Bot for resource <"+ info.getPath() +"> DEAD");
        }
    }
}
