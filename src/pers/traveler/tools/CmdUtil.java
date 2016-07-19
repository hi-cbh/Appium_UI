package pers.traveler.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by quqing on 15/9/29.
 * 运行命令行
 */
public class CmdUtil {
    /**
     * 调用并执行控制台命令
     *
     * @param cmd 控制台命令
     * @return output
     * @author quqing
     */
    public static String run(String cmd) {
        String line;
        String cmdOut = "";
        Process p;
        BufferedReader br = null;
        System.out.println("命令行运行前的打印： " + cmd);
        try {
            p = init(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = br.readLine()) != null) {
                cmdOut = cmdOut + line + System.getProperty("line.separator");
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
        System.out.println("-------------------------------------------- ");
        System.out.println("命令行运行后结果输出： " + cmdOut);
        System.out.println("-------------------------------------------- ");
        return null == cmdOut ? null : cmdOut.trim();
    }

    public static Process init(String cmd) {
        Process p = null;
        try {
            if (isWindows()) {
                String command = "cmd /c " + cmd;
                System.out.println("命令行运行： " + command);
                p = Runtime.getRuntime().exec(command);
            } else {
                String[] shell = {"sh", "-c", cmd};
                p = Runtime.getRuntime().exec(shell);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * 判断是否Windows操作系统
     *
     * @return 是否windows系统
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return (os.toLowerCase().startsWith("win")) ? true : false;
    }
}