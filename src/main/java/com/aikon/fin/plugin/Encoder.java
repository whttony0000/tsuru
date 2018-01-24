package com.aikon.fin.plugin;

import java.util.List;

/**
 * @author haitao.wang
 */
public interface Encoder {

    List<String> encode(List<String> srcList);

    String encode(String src);
}
