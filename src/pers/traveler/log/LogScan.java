package pers.traveler.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import pers.traveler.tools.CmdUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 这个是干嘛的
 * @author Administrator
 *
 */
public class LogScan extends Thread {
    private String cmd;

    public LogScan(String cmd) {
    	System.out.println("LogScan 构造函数");
        this.cmd = cmd;
    }

    @Override
    public void run() {
    	System.out.println("LogScan run");
        cmdInvoke(cmd);
    }

    /**
     * 这里是干什么的
     * @param cmd
     */
    private void cmdInvoke(String cmd) {
    	
        BufferedReader br = null;
        Process p;
        String filePath = "config" + File.separator + "log4j.properties";

        try {
            Logger logger = Logger.getLogger("app_log");
            PropertyConfigurator.configure(new File(filePath).getAbsolutePath());
            if (CmdUtil.isWindows()) {
                String command = "cmd /c " + cmd;
                p = Runtime.getRuntime().exec(command);
            } else {
                String[] shell = {"sh", "-c", cmd};
                p = Runtime.getRuntime().exec(shell);
            }
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                logger.info(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}