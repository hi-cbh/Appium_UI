package pers.traveler.robot;

import io.appium.java_client.android.AndroidDriver;
import pers.quq.filedb.core.FileFilterImpl;
import pers.quq.filedb.core.Filter;
import pers.traveler.constant.PlatformName;
import pers.traveler.constant.Type;
import pers.traveler.device.DeviceFactory;
import pers.traveler.engine.EngineFactory;
import pers.traveler.entity.UiNode;
import pers.traveler.log.Log;
import pers.traveler.tools.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Stack;

/**
 * Created by quqing on 16/5/11.
 */
public class AndroidRobot extends Robot {
	/**
	 * 将配置文件传给父类
	 * @param configFile
	 */
    public AndroidRobot(String configFile) {
        super(configFile);
    }

    
    @Override
    protected String getRemoveApp() {
    	//获取removeApp
        return config.getAppPackage();
    }

    /**
     * 从日志提取错误信息
     */
    @Override
    protected void catchAppException() {
        String path;
        String exceptionInfo;
        Filter filter = new FileFilterImpl();

//        try {
//        	//保存路径
//            path = "output" + File.separator + date + File.separator + time 
//            		+ File.separator + deviceID + File.separator + "logs" + File.separator;
//            
//            System.out.println("catchAppException path " + path);
//            
//            //过滤app.log搜索"crash"
//            exceptionInfo = filter.grep(path + "app.log", pers.traveler.constant.Filter.CRASH);
//           
//            System.out.println("exceptionInfo1 " + exceptionInfo);
//            //过滤app.log搜索"System.err"
//            exceptionInfo = exceptionInfo + System.getProperty("line.separator") + 
//            		filter.grep(path + "app.log", pers.traveler.constant.Filter.SYSTEM_ERR);
//            System.out.println("exceptionInfo " + exceptionInfo);
//            //写入文件
//            FileUtil.writeAll(path + "app_err.log", exceptionInfo);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void working() {
    	System.out.println("AndroidRobot working ");
        String homePageSource;
        List<String> guideFlow;
        Stack<UiNode> taskStack;

        try {
        	//使用工厂模式，创建对象
            device = DeviceFactory.createDevice(PlatformName.Android, config.getUdid());
            //获取PageSource
            homePageSource = beforeTravel();
            //获取引导流操作
            guideFlow = config.getGuideFlow();

            String pack = device.getAppPackage(config.getApp());
            System.out.println("pack " + pack);
            
            Log.logInfo("########################### app environment ###########################");
            Log.logInfo("product = " + device.getProductModel());
            Log.logInfo("api level = " + device.getApiLevel());
            Log.logInfo("platform version = " + device.getPlatformVersion());
            Log.logInfo("core version = " + device.getCoreVersion());
            Log.logInfo("system version = " + device.getSysVersion());
            Log.logInfo("version = " + device.getVersion());
            Log.logInfo("app = " + pack);
            Log.logInfo("app activity = " + device.getAppActivity(config.getApp()));
            Log.logInfo("app current activity = " + device.getCurrentActivity());
            Log.logInfo("app is alive = " + device.appIsAlive(pack));
            Log.logInfo("language = " + device.getLanguage());
            Log.logInfo("resolution = " + device.getResolution());
            Log.logInfo("uiautomator pid = " + device.getUiautomatorProcess());
            Log.logInfo("getLogCatPID " + device.getLogCatPID());
            Log.logInfo("########################################################################");

            engine = EngineFactory.build(PlatformName.Android, driver, config);

            
//            //判断应用是否已安装
//            if (((AndroidDriver) driver).isAppInstalled(getRemoveApp()))
//                logManager.run(config);  //调用logManager

            if (null != guideFlow && guideFlow.size() > 0){
            	//System.out.println("getInterval: "+ config.getInterval());
            	//System.out.println("guideFlow: " + guideFlow);
            	homePageSource = engine.guideRuleProcessing(guideFlow, config.getInterval());
            	//System.out.println("homePageSource: " + homePageSource);
            	
            }
            	
                
            	//遍历模式
            if (runMode == 1) {
                taskStack = engine.getTaskStack(Type.XML, homePageSource, 1);

                Log.logInfo("########################### 开始执行探索性遍历测试 ###########################");
                engine.dfsSearch(taskStack, config.getDepth());
            }

            afterTravel();
        } catch (ClassNotFoundException e) {
            Log.logError(e.fillInStackTrace());
        } catch (NoSuchMethodException e) {
            Log.logError(e.fillInStackTrace());
        } catch (IllegalAccessException e) {
            Log.logError(e.fillInStackTrace());
        } catch (InvocationTargetException e) {
            Log.logError(e.fillInStackTrace());
        } catch (InstantiationException e) {
            Log.logError(e.fillInStackTrace());
        }
    }
}
