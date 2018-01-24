package com.aikon.fin.plugin;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author haitao.wang
 */
@Slf4j
public class UesEncoder implements Encoder {


    public UesEncoder() {
    }

    @Override
    public List<String> encode(List<String> srcList) {
        return srcList;
    }

    @Override
    public String encode(String src) {
        return src;

    }

}

