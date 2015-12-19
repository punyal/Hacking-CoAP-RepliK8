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

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se> <pablo@punyal.com>
 */
public class ResourceInfo {
    private final String title;
    private final String path;
    private final String att_ct;
    private final String att_rt;
    private final String att_sz;
    private final String att_if;
    private final boolean obs;
    
    public ResourceInfo(String path, String title, String att_ct, String att_rt, String att_sz, String att_if, boolean obs) {
        this.title = title;
        this.path = path;
        this.att_ct = att_ct;
        this.att_rt = att_rt;
        this.att_sz = att_sz;
        this.att_if = att_if;
        this.obs = obs;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(path).append(">");
        if (obs) sb.append(";obs");
        if (att_ct != null) sb.append(";ct=").append(att_ct);
        if (att_if != null) sb.append(";if=").append(att_if);
        if (att_rt != null) sb.append(";rt=").append(att_rt);
        if (att_sz != null) sb.append(";sz=").append(att_sz);
        if (title != null) sb.append(";title=\"").append(title).append("\"");
        return sb.toString();
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getCT() {
        return att_ct;
    }
    
    public String getRT() {
        return att_rt;
    }
    
    public String getSZ() {
        return att_sz;
    }
    
    public String getIF() {
        return att_if;
    }
    
    public boolean isObservable() {
        return obs;
    }
    
}
