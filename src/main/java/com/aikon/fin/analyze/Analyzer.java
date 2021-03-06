package com.aikon.fin.analyze;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author haitao.wang
 */
public interface Analyzer {

    <T> T handleLine(String line);

    boolean checkLine(String line);

    String preHandleLine(String line);

    default <T> List<T> analyze(String path) throws IOException {
        File file = new File(path);
        List<T> lineObjList;
        lineObjList = Files.readLines(file, Charsets.UTF_8, new LineProcessor<List<T>>() {
            List<T> lineObjCollection = Lists.newLinkedList();

            public boolean processLine(String line) {
                if (StringUtils.isBlank(line)) {
                    return true;
                }
                line = preHandleLine(line);
                if (!checkLine(line)) {
                    return true;
                }
                T lineObj = handleLine(line);
                if (lineObj == null) {
                    return true;
                }
                lineObjCollection.add(lineObj);
                return true;
            }

            public List<T> getResult() {
                return lineObjCollection;
            }
        });
        return lineObjList;
    }

}
