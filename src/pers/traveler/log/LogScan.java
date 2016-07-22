package pers.traveler.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import pers.traveler.tools.CmdUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 配置文件，并将日志打印和写入文件
 * 
 * 继承了Thread，自动运行run开启子线程
 */
public class LogScan extends Thread {
    private String cmd;

    /**
     * 导入配置文件，并将日志打印和写入文件
     * @param cmd
     */
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
     * 这个是读取log4j.properties 配置文件，并将日志打印和写入文件
     * @param cmd
     */
    private void cmdInvoke(String cmd) {
    	
        BufferedReader br = null;
        Process p;
        String filePath = "config" + File.separator + "log4j.properties";

        try {
        	//获取日志记录器，这个记录器将负责控制日志信息
            Logger logger = Logger.getLogger("app_log");
            /**
             * BasicConfigurator.configure ()： 自动快速地使用缺省Log4j环境。  
			 * PropertyConfigurator.configure ( String configFilename) ：读取使用Java的特性文件编写的配置文件。  
			 * DOMConfigurator.configure ( String filename ) ：读取XML形式的配置文件。
             */
            //读取使用Java的特性文件编写的配置文件。  
            PropertyConfigurator.configure(new File(filePath).getAbsolutePath());
            //判断运行环境
            if (CmdUtil.isWindows()) {
                String command = "cmd /c " + cmd;
                //方法主要用于执行外部的程序或命令
                p = Runtime.getRuntime().exec(command);
            } else {
                String[] shell = {"sh", "-c", cmd};
                p = Runtime.getRuntime().exec(shell);
            }
            /**
             * 简单说明：
             * getInputStream() 获取子进程的输入流
             * InputStreamReader这个对象是处理流，字符流，输入流
             * BufferedReader的类型是缓冲处理流、字符流、输入流
             * 
             * 详细说明：
             * 1.InputStream、OutputStream处理字节流的抽象类
			   InputStream 是字节输入流的所有类的超类,一般我们使用它的子类,如FileInputStream等.
			   OutputStream是字节输出流的所有类的超类,一般我们使用它的子类,如FileOutputStream等.

			   2.InputStreamReader  OutputStreamWriter处理字符流的抽象类
			   InputStreamReader 是字节流通向字符流的桥梁,它将字节流转换为字符流.
			   OutputStreamWriter是字符流通向字节流的桥梁，它将字符流转换为字节流.

			   3.BufferedReader BufferedWriter
			   BufferedReader 由Reader类扩展而来，提供通用的缓冲方式文本读取，readLine读取一个文本行，从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取。
			   BufferedWriter  由Writer 类扩展而来，提供通用的缓冲方式文本写入， newLine使用平台自己的行分隔符，将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入。

			实例：
               InputStream能从來源处读取逐个字节,所以它是最低级的。
               InputStreamReader封裝了InputStream,一次读取一个一个字符
               BufferedReader则是比InputStreamReader更高级,它封裝了StreamReader类,一次读取取一行的字符
             */
            
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
            	//插入记录信息（格式化日志信息）
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