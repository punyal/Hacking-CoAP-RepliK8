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

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class CoapPhantomResource extends CoapResource {

    public CoapPhantomResource(ResourceInfo info) {
        super(info.getPath());
        getAttributes().setTitle(info.getTitle());
        
        if (info.isObservable()) {
            setObservable(true);
            getAttributes().setObservable();
            setObserveType(CoAP.Type.NON); // TODO: Autodetect Type
            // TODO: Observe the resource and triger subscriptions
        }
    }
    
    @Override
    public void handleGET(CoapExchange exchange) {
        ResponseCode responseCode = ResponseCode.UNSUPPORTED_CONTENT_FORMAT;
        byte[] payload = null;
        int contentFormat = MediaTypeRegistry.UNDEFINED;
        
        exchange.respond(responseCode, payload, contentFormat);
    }
    
    @Override
    public void handlePUT(CoapExchange exchange) {
        
    }
    
    @Override
    public void handlePOST(CoapExchange exchange) {
        
    }
    
    @Override
    public void handleDELETE(CoapExchange exchange) {
        
    }
    
}
