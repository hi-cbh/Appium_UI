package pers.traveler.test;

import pers.traveler.constant.PlatformName;
import pers.traveler.robot.Robot;
import pers.traveler.robot.RobotFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mac on 16/5/16.
 */
public class TravelerTest {
	private static String workspace_path;
	
    public static void main(String[] args) throws InterruptedException {
        Robot testRobot;
        workspace_path = getWorkSpase();
        try {
            testRobot = RobotFactory.build(PlatformName.Android, workspace_path+ "\\config\\android.xml");
            //testRobot = RobotFactory.build(PlatformName.iOS, "/Users/mac/Documents/intelliJ-idea/AutoTraveler/config/ios.xml");
            testRobot.travel();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 获取当前运行工作空间
     * @return
     */
	public static String getWorkSpase() {
		File directory = new File("");
		String abPath = directory.getAbsolutePath();
		return abPath;
	}
    
}