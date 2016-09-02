package pers.traveler.log;

import pers.traveler.constant.CmdConfig;
import pers.traveler.device.AndroidDevice;
import pers.traveler.device.Device;
import pers.traveler.entity.Config;
import pers.traveler.tools.CmdUtil;

import java.util.List;

/**
 * Created by quqing on 16/5/19.
 * 日志管理类
 */
public class LogManager {
    private LogScan logScan;

    public void run(Config config) {
    	System.out.println("LogManager run");
    	//实例化 LogScan对象，并传入
        logScan = new LogScan(config.getLogCmd().replaceAll("#udid#", config.getUdid()));
        //adb -s 0bd08bcc logcat -b main -b system -b events -b radio *:D| grep contacts
        
        //这里有EBUG，用这个无法打印应用log
        //adb shell logcat -b main -b system -b events -b radio *:D| grep contacts

        //开启子线程，后台执行
        logScan.start();
    }

    //停止进程
    public void stop(Device device, String udid) {
    	System.out.println("LogManager stop");
        String killCmd;
        List<String> pidList = device.getLogCatPID();
        killCmd = device instanceof AndroidDevice ? CmdConfig.KILL_APP_PROCESS : CmdConfig.KILL_SYS_PROCESS;
        if (null != pidList) {
            for (String pid : pidList) {
                killCmd = killCmd.replaceAll("#udid#", udid).replaceAll("#pid#", pid);
                System.out.println("LogManager udid" + udid);
                System.out.println("LogManager pid" + pid);
                System.out.println("LogManager stop " + killCmd);
                CmdUtil.run(killCmd);
            }
        }
    }
}
