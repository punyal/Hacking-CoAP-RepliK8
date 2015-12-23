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
import com.punyal.replik8.Parsers;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public abstract class CoapObserver {
    private final Configuration configuration;
    private final ResourceInfo info;
    private CoapClient coapClient;
    private final CoapHandler coapHandler;
    private CoapObserveRelation relation;
    
    public CoapObserver(Configuration configuration, ResourceInfo info) {
        this.configuration = configuration;
        this.info = info;
        coapHandler = new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                incomingData(response);
            }
            @Override
            public void onError() {
                error();
            }
        };
    }
    
    public void startObserve() {
        coapClient = new CoapClient(Parsers.generateURI(configuration.getRemoteAddress(), configuration.getRemotePort(), info.getPath()));
        coapClient.setTimeout(10000);
        //System.out.println("Starting Observe: "+coapClient.getURI());
        relation = coapClient.observe(coapHandler);
    }
    
    public void stopObserver() {
        //System.out.println("Stoping Observe: "+coapClient.getURI());
        if (relation!=null) relation.reactiveCancel();
    }
    
    abstract public void incomingData(CoapResponse response);
    
    abstract public void error();
}
