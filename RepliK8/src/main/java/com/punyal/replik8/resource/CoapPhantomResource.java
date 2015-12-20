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
import java.util.Observable;
import java.util.Observer;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class CoapPhantomResource extends CoapResource {
    private ResourceInfo info;
    private final ResourceRequestBot bot;
    
    private Observable obClass;
    
    public CoapPhantomResource(Configuration configuration, ResourceInfo info) {
        super(info.getPath().substring(1));
        System.out.println("creating resource <"+info.getPath()+">");
        getAttributes().setTitle(info.getTitle());
        SimpleEventNotifier notifier = null;
        
        if (info.isObservable()) {
            setObservable(true);
            getAttributes().setObservable();
            setObserveType(CoAP.Type.NON); // TODO: Autodetect Type
            notifier = new SimpleEventNotifier();
            notifier.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    changed();
                }
            });
        }
        
        bot = new ResourceRequestBot(configuration, info, notifier);
        bot.startBot();
        
        // TODO: Create event base notification for observe
        /*
            changed();
        */
    }
    
    @Override
    public void handleGET(CoapExchange exchange) {
        CoapResponse response = bot.getResponseGET();
        System.out.println(response.getCode().name() +" - "+ response.getOptions().getContentFormat() +" - "+ response.getResponseText());
        exchange.respond(response.getCode(), response.getPayload(), response.getOptions().getContentFormat());
    }
    
    /* TODO:Implement this after GET
    @Override
    public void handlePUT(CoapExchange exchange) {
        
    }
    
    @Override
    public void handlePOST(CoapExchange exchange) {
        
    }
    
    @Override
    public void handleDELETE(CoapExchange exchange) {
        
    }
    */
}
