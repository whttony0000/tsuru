package com.aikon.fin.plugin;

/**
 * @author haitao.wang
 */
public class UesDecoder implements Decoder {


    public UesDecoder() {
    }

    @Override
    public String decode(String ticket) {
        return ticket;
    }
}
