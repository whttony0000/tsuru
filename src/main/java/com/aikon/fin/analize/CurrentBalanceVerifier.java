package com.aikon.fin.analize;

import com.aikon.fin.entity.CurrentBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haitao.wang
 */
@Slf4j
public class CurrentBalanceVerifier {

    public static void main(String[] args) {
        CurrentBalanceVerifier verifier = new CurrentBalanceVerifier();
        verifier.verify("csv/dpm_2018_01_01.csv","csv/sina_2018_01_01.csv");
    }



    public void verify(String dpmFilePath, String sinaFilePath) {
        Analyzer dpmFileAnalyzer = new DpmFileAnalyzer();
        Analyzer sinaFileAnalyzer = new SinaFileAnalyzer();

        List<CurrentBalance> dpmCurrentBalances = null;
        List<CurrentBalance> sinaCurrentBalances = null;
        try {
            dpmCurrentBalances = dpmFileAnalyzer.analyze(Resources.getResourceAsFile(dpmFilePath).getPath());
            sinaCurrentBalances = sinaFileAnalyzer.analyze(Resources.getResourceAsFile(sinaFilePath).getPath());
        } catch (IOException e) {
            log.info("Error Load Files {}",e);
        }
        List<CurrentBalance> finalSinaCurrentBalances = sinaCurrentBalances;
        List<CurrentBalance> finalDpmCurrentBalances = dpmCurrentBalances;
        List<CurrentBalance> intersection = finalSinaCurrentBalances.stream().filter(dpmItem -> !finalDpmCurrentBalances.contains(dpmItem)).collect(Collectors.toList());
        intersection.stream().forEach(item -> System.out.println(item.getMemberId() + " " + item.getBalance()));
    }
}
