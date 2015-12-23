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
import com.punyal.replik8.Parsers;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class ResourceRequestBot extends Thread {
    private static final Logger log = Logger.getLogger(ResourceRequestBot.class.getName());
    private final Configuration configuration;
    private final ResourceInfo info;
    private boolean running = false;
    
    private final SimpleEventNotifier notifier;
    
    private CoapResponse responseGET = null;
    private CoapResponse responsePUT = null;
    private CoapResponse responsePOST = null;
    private CoapResponse responseDELETE = null;
    
    public ResourceRequestBot(Configuration configuration, ResourceInfo info, SimpleEventNotifier notifier) {
        this.configuration = configuration;
        this.info = info;
        this.notifier = notifier;
        this.setDaemon(true);
    }
    
    public void startBot() {
        running = true;
        start();
    }
    
    public void stopBot() {
        running = false;
    }
    
    public CoapResponse getResponseGET() {
        return responseGET;
    }
    
    public CoapResponse getResponsePUT() {
        return responsePUT;
    }
    
    public CoapResponse getResponsePOST() {
        return responsePOST;
    }
    
    public CoapResponse getResponseDELETE() {
        return responseDELETE;
    }
    
    @Override
    public void run() {
        CoapClient coapClient;
        CoapObserver observer = null;
        String uri;
        try {
            // Observable GET
            if (info.isObservable()) {
                observer = new CoapObserver(configuration, info) {
                    
                    @Override
                    public void incomingData(CoapResponse response) {
                        //System.out.println("New data: "+ response.getResponseText());
                        responseGET = response;
                        if (notifier != null) {
                            notifier.setChanged();
                            notifier.notifyObservers();
                        }
                    }
                    
                    @Override
                    public void error() {
                        log.log(Level.WARNING, "Error observing resource: "+ info.getPath());
                    }
                };
                observer.startObserve();
                
            }
            while (running) {
                if (!info.isObservable()) { // Non-Observable GET
                    coapClient = new CoapClient(Parsers.generateURI(configuration.getRemoteAddress(), configuration.getRemotePort(), info.getPath()));
                    // TODO: Check if the previous response has an error code
                    //System.out.println("GET: "+coapClient.getURI());
                
                    responseGET = coapClient.get();
                    if (responseGET == null) {
                    log.log(Level.FINE, "Resource <{0}> no response for method GET", info.getPath());
                    } 
                }
                
                
                /*
                TODO: Implement POST, PUT and DELETE methods
                */
                
                
                try {
                    Thread.sleep(Constants.DEFAULT_REQUEST_TIMEOUT);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            if (observer != null) observer.stopObserver();
            log.log(Level.WARNING, "Bot for resource <{0}> DEAD", info.getPath());
        }
    }
}
