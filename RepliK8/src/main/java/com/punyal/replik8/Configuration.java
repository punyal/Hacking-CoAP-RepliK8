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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class Configuration {
    private static final Logger log = Logger.getLogger(Configuration.class.getName());
    private final String remoteURI;
    private InetAddress remoteAddress = null;
    private int remotePort;
    private final int localPort;
    
    public Configuration(InetAddress remoteAddress, int remotePort, int localPort) {
        this.remoteURI = null;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.localPort = localPort;
    }
    
    public Configuration(String remoteURI, int localPort) {
        this.remoteURI = remoteURI;
        this.localPort = localPort;
        
        // Parse remote Address and Port
        try {
            remoteAddress = InetAddress.getByName(Parsers.getHostFromURI(remoteURI));
        } catch (UnknownHostException e) {
            log.log(Level.SEVERE, "Problems to set the Host by Name", e);
        }
        remotePort = Parsers.getPortFromURI(remoteURI);
    }
    
    public int getLocalPort() {
        return localPort;
    }
    
    public int getRemotePort() {
        return remotePort;
    }
    
    public InetAddress getRemoteAddress() {
        return remoteAddress;
    }
}
