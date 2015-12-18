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

import java.net.Inet6Address;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class RepliK8 implements Runnable {
    private static final Logger log = Logger.getLogger(RepliK8.class.getName()); 
    private final Configuration configuration;
    private boolean running = true;
    
    public RepliK8(Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public void run() {
        State state = State.CHECK_CONFIGURATION;
        try {
            while (running) {
                try {
                    Thread.sleep(1000); // Sleep 1s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
                
                switch (state) {
                    case CHECK_CONFIGURATION:
                        System.out.println("\nRepliK8 Configuration:\n - Remote Address: "+configuration.getRemoteAddress()
                                + "\n - Remote Port: "+configuration.getRemotePort()
                                + "\n - Local Port: "+configuration.getLocalPort()+"\n");
                        
                        state = State.GET_RESOURCES_LIST;
                        break;
                    case GET_RESOURCES_LIST:
                        String uri;
                        if (configuration.getRemoteAddress() instanceof Inet6Address)
                            uri = "coap://["+configuration.getRemoteAddress().getHostAddress()+"]:"+configuration.getRemotePort()+"/.well-known/core";
                        else
                            uri = "coap://"+configuration.getRemoteAddress().getHostAddress()+":"+configuration.getRemotePort()+"/.well-known/core";
                        CoapClient coapClient = new CoapClient(uri);
                        CoapResponse response = coapClient.get();
                        if (response != null) {
                            List<Resource> resourcesList = Parsers.parseWellKnownCore(response.getResponseText());
                            
                            System.out.println("Total number of resources: "+resourcesList.size());
                            for(Resource resource: resourcesList) {
                                System.out.print("<"+resource.getPath()+">");
                            }
                            System.out.print("\n");
                            
                        } else {
                            log.log(Level.WARNING, "No connection to {0}", uri);
                            running = false;
                        }
                        state = State.REQUEST_EACH_RESOURCE;
                        break;
                    default:
                        running = false;
                        break;
                }
                
                
            }
        } finally {
            log.log(Level.SEVERE, "Ending RepliK8");
        }
    }
    
    public enum State {
        CHECK_CONFIGURATION,
        GET_RESOURCES_LIST,
        REQUEST_EACH_RESOURCE
        
    }
    
}
