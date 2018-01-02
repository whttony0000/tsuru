package com.aikon.fin.analize;

import java.util.List;

/**
 * @author haitao.wang
 */
public interface Analyzer {

    <T> T handleLine(String line);

    <T> List<T> analyze(String path);
}
