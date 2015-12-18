/*
 * The MIT License
 *
 * Copyright 2015 Pablo Puñal Pereira <pablo.punal@ltu.se>.
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();
    private Configuration configuration;
    
    public Cli(String[] args) {
        this.args = args;
        // Cli options
        options.addOption("h", "help", false, "Show help.");
        options.addOption("p", "localport", true, "Set local port.");
        options.addOption("u", "uri", true, "Set URI to replik8.");
    }
    
    public void parse() {
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h"))
                help();
            if (cmd.hasOption("u")) {
                if (cmd.hasOption("p"))
                    setConfiguration(cmd.getOptionValue("u"), Integer.parseInt(cmd.getOptionValue("p")));
                else
                    setConfiguration(cmd.getOptionValue("u"));
            } else {
                help();
            }
            
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
        }
    }
    
    private void help() {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("RepliK8", options);
        System.exit(0);
    }
    
    private void setConfiguration(String remoteURI, int localPort) {
        configuration = new Configuration(remoteURI, localPort);
    }
    
    private void setConfiguration(String remoteURI) {
        setConfiguration(remoteURI, Constants.DEFAULT_COAP_PORT);
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
}
