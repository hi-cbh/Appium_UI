package pers.traveler.robot;

import pers.traveler.constant.Package;

import java.lang.reflect.InvocationTargetException;

/**
 * 使用工厂模式，创建对象
 * Created by quqing on 16/5/18.
 * 机器人工厂
 * 
 * </p>1、将ROBOT = "pers.traveler.robot.#type#Robot"替换为"pers.traveler.robot.AndroidRobot"; 
 * </p>2、Class.forName(xxx.xx.xx);的作用是要求JVM查找并加载指定的类，也就是说JVM会执行该类的静态代码段
 * </p>3、Class还有一个有用的方法可以为类创建一个实例，这个方法叫做newInstance()。例如：     x.getClass.newInstance()，
 * </p>4、创建了一个同x一样类型的新实例。newInstance()方法调用默认构造器（无参数构造器）初始化新建对象。
 * </p>5、最后用最简单的描述来区分new关键字和newInstance()方法的区别： 
	</p>	newInstance: 弱类型。低效率。只能调用无参构造。 
	</p>	new: 强类型。相对高效。能调用任何public构造。
 * 
 */
public class RobotFactory {
    public static Robot build(String robotType, String configFile) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    	//Class.forName(xxx.xx.xx) 返回的是一个类: 
    	Class clazz = Class.forName(Package.ROBOT.replaceAll("#type#", robotType));
    	/**
    	 * 1、Constructor类代表某个类中的一个构造方法-> 调用AndroidRobot类的构造方法
    	 * 2、创建实例对象：Class.newInstance()方法 -> 实现创建对象并传参
    	 */
        return (Robot) clazz.getConstructor(String.class).newInstance(configFile);
    }
}
