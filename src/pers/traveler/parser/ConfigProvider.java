package pers.traveler.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pers.traveler.constant.CmdConfig;
import pers.traveler.constant.PlatformName;
import pers.traveler.entity.Config;
import pers.traveler.exception.XmlParseException;
import pers.traveler.tools.DateUtil;
import pers.traveler.log.Log;
import pers.traveler.tools.MD5Generator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by quqing on 16/5/5.
 */
public class ConfigProvider {

    public String getMD5ByString(String string) throws Exception {
        return MD5Generator.getMD5ByString(string);
    }

    public List<String> getModule(String fileName) {
        Node node;
        List<String> moduleFlow;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);

            node = document.getElementsByTagName("module").item(0);
            moduleFlow = getList(node);

            return moduleFlow;

        } catch (ParserConfigurationException e) {
            Log.logError(e.fillInStackTrace());
        } catch (SAXException e) {
            Log.logError(e.fillInStackTrace());
        } catch (IOException e) {
            Log.logError(e.fillInStackTrace());
        }
        return null;
    }

    //获取config配置文件
    public Config getConfig(String fileName) {
    	//声明默认参数
        byte mode = 1, runMode = 1, depth = 0, identifyDefault = 8, filter = 3, reverse = 1, allowSameWinTimes = 15;
        int port = 4730;
        long timeout = 30, duration = 0, interval = 0;
        String host = "127.0.0.1";
        String sMode, sRunMode, sDepth, sPort, sDuration, sInterval, sTimeout, sIdentifyDefault, sFilter, sReverse, sAllowSameWinTimes;
        String app = null, udid = null, tips = null, appPackage = null, capabilityType = null, logCmd = null, bundleId = null, runServer = null;
        String screenshot = "screenshot" + File.separator + DateUtil.formatTime() + File.separator;
        List<String> backList = null;
        List<String> identifySpecialList, clickList, inputList, triggerList, blackList, guideFlow, notBackList;
        Map<String, String> capabilityMap = new HashMap<>();
        Node node;
        NodeList nodeList;
        //声明一个config对象
        Config config = new Config();
        //调用 DocumentBuilderFactory.newInstance() 方法得到创建 DOM 解析器的工厂
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
        	//调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过文件的方式获取Document对象 ,解析指定的文件
            Document document = db.parse(fileName);

            if (null != document.getElementsByTagName("identifyDefault").item(0)) {
                sIdentifyDefault = document.getElementsByTagName("identifyDefault").item(0).getTextContent();
                if (null != sIdentifyDefault && !sIdentifyDefault.isEmpty()) {
                	//Byte.parseByte此方法将返回由十进制参数表示的字节值
                    identifyDefault = Byte.parseByte(sIdentifyDefault);
                }
            }

            if (null != document.getElementsByTagName("filter").item(0)) {
                sFilter = document.getElementsByTagName("filter").item(0).getTextContent();
                if (null != sFilter && !sFilter.isEmpty())
                    filter = Byte.parseByte(sFilter.trim());
            }

            if (null != document.getElementsByTagName("mode").item(0)) {
                sMode = document.getElementsByTagName("mode").item(0).getTextContent();
                if (null != sMode && !sMode.isEmpty())
                    mode = Byte.parseByte(sMode.trim());
            }

            if (null != document.getElementsByTagName("runMode").item(0)) {
                sRunMode = document.getElementsByTagName("runMode").item(0).getTextContent();
                if (null != sRunMode && !sRunMode.isEmpty())
                    runMode = Byte.parseByte(sRunMode.trim());
            }

            if (null != document.getElementsByTagName("reverse").item(0)) {
                sReverse = document.getElementsByTagName("reverse").item(0).getTextContent();
                if (null != sReverse && !sReverse.isEmpty())
                    reverse = Byte.parseByte(sReverse.trim());
            }

            if (null != document.getElementsByTagName("depth").item(0)) {
                sDepth = document.getElementsByTagName("depth").item(0).getTextContent();
                if (null != sDepth && !sDepth.isEmpty())
                    depth = Byte.parseByte(sDepth);
            }

            if (null != document.getElementsByTagName("allowSameWinTimes").item(0)) {
                sAllowSameWinTimes = document.getElementsByTagName("allowSameWinTimes").item(0).getTextContent();
                if (null != sAllowSameWinTimes && !sAllowSameWinTimes.isEmpty())
                    allowSameWinTimes = Byte.parseByte(sAllowSameWinTimes);
            }

            if (null != document.getElementsByTagName("port").item(0)) {
                sPort = document.getElementsByTagName("port").item(0).getTextContent();
                if (null != sPort && !sPort.isEmpty())
                	//Integer.parseInt此方法返回由十进制参数表示的整数值。
                    port = Integer.parseInt(sPort);
            }

            if (null != document.getElementsByTagName("duration").item(0)) {
                sDuration = document.getElementsByTagName("duration").item(0).getTextContent();
                if (null != sDuration && !sDuration.isEmpty())
                	//Long.parseLong此方法返回由十进制参数表示的整数值。
                    duration = Long.parseLong(sDuration.trim());
            }

            if (null != document.getElementsByTagName("interval").item(0)) {
                sInterval = document.getElementsByTagName("interval").item(0).getTextContent();
                if (null != sInterval && !sInterval.isEmpty())
                    interval = Long.parseLong(sInterval.trim());
            }

            if (null != document.getElementsByTagName("timeout").item(0)) {
                sTimeout = document.getElementsByTagName("timeout").item(0).getTextContent();
                if (null != sTimeout && !sTimeout.isEmpty())
                    timeout = Long.parseLong(sTimeout);
            }

            if (null != document.getElementsByTagName("host").item(0)) {
                host = document.getElementsByTagName("host").item(0).getTextContent();
            }

            if (null != document.getElementsByTagName("tips").item(0)) {
                tips = document.getElementsByTagName("tips").item(0).getTextContent();
            }

            if (null != document.getElementsByTagName("screenshot").item(0)) {
            	//这里获取截图路径，并配置具体路径File.separator为 路径的“\”
                //screenshot = document.getElementsByTagName("screenshot").item(0).getTextContent() + "\\" + DateUtil.formatTime() + "\\";
                screenshot = document.getElementsByTagName("screenshot").item(0).getTextContent() + File.separator + DateUtil.formatTime() + File.separator;
            }

            if (null != document.getElementsByTagName("runServer").item(0)) {
                runServer = document.getElementsByTagName("runServer").item(0).getTextContent();
            }

            node = document.getElementsByTagName("capability").item(0);
            if (null != node) {
                nodeList = node.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                	//如果当前节点是元素节点的话
                    if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                        capabilityMap.put(nodeList.item(i).getNodeName().trim(), nodeList.item(i).getTextContent().trim());
                }
            }

            if (mode == PlatformName.ANDROID) {
                capabilityType = "androidCapability";
                //获取的内容是什么
                node = document.getElementsByTagName("androidGuideFlow").item(0);
                //printNode(node);
                /**
                 * 节点打印：
                 * step: click>>com.chinamobile.contacts.im:id/skip_btn1
				 * step: click>>com.chinamobile.contacts.im:id/dialog_btn_positive
                 * 
                 */
                
                if (null != document.getElementsByTagName("android").item(0)) {
                    logCmd = document.getElementsByTagName("android").item(0).getTextContent();
                    System.out.println("logCmd true: " + logCmd);
                    //显示为：adb -s #udid# logcat -b main -b system -b events -b radio *:D| grep contacts
                } else {
                    logCmd = CmdConfig.LOG_ANDROID_CMD;
                    System.out.println("logCmd false: " + logCmd);
                }
            } else if (mode == PlatformName.IOS) {
                capabilityType = "iosCapability";
                node = document.getElementsByTagName("iosGuideFlow").item(0);
                if (null != document.getElementsByTagName("ios").item(0)) {
                    logCmd = document.getElementsByTagName("ios").item(0).getTextContent();
                } else {
                    logCmd = CmdConfig.LOG_IOS_CMD;
                }
            } else if (mode == PlatformName.WEB) {
                capabilityType = "webCapability";
                node = document.getElementsByTagName("webGuideFlow").item(0);
                if (null != document.getElementsByTagName("web").item(0)) {
                    logCmd = document.getElementsByTagName("web").item(0).getTextContent();
                } else {
                    logCmd = null;
                }
            }
            
            //这个返回什么
            guideFlow = getList(node);
           
            
            //测试数据
//            System.out.println("guideFlow");
//            for(int tmp =0; tmp < guideFlow.size(); tmp++){
//            	System.out.println(guideFlow.get(tmp));
//            }
            /**
             * 内容是：
             * click>>com.chinamobile.contacts.im:id/skip_btn1
			 * click>>com.chinamobile.contacts.im:id/dialog_btn_positive
             */
            
            //注意 capabilityType = androidCapability
            node = document.getElementsByTagName(capabilityType).item(0);
            //printNode(node);
            //节点内容：
            /**
             * app: D:/android_apk/mcontact_4.0.0.apk
				udid: 0bd08bcc
				platformName: Android
				platformVersion: 4.3
				deviceName: GT_I9308I
				appPackage: com.chinamobile.contacts.im
				appActivity: com.chinamobile.contacts.im.LoadingPage
				unicodeKeyboard: true
				resetKeyboard: true
				noSign: true
             */
            
            //node又是什么
            if (null != node) {
                nodeList = node.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                        capabilityMap.put(nodeList.item(i).getNodeName().trim(), nodeList.item(i).getTextContent().trim());
                }
            } else {
                throw new XmlParseException("the tag of <" + capabilityType + "> can't be empty");
            }

            if (mode == PlatformName.ANDROID || mode == PlatformName.IOS) {
                app = capabilityMap.get("app");
                udid = capabilityMap.get("udid");
                if (null == app)
                    throw new XmlParseException("the tag of <app> can't be empty");
//                if (null == udid)
//                    throw new XmlParseException("the tag of <udid> can't be empty");
                if (mode == PlatformName.ANDROID) {
                    appPackage = capabilityMap.get("appPackage");
                    if (null == appPackage)
                        throw new XmlParseException("the tag of <appPackage> can't be empty");
                }

                if (mode == PlatformName.IOS) {
                    bundleId = capabilityMap.get("bundleId");
//                    if (null == bundleId)
//                        throw new XmlParseException("the tag of <bundleId> can't be empty");
                }
            }

            node = document.getElementsByTagName("click").item(0);
            if (null != node) {
                clickList = getList(node);
            } else {
                throw new XmlParseException("the tag of <click> following <rule> can't be empty");
            }

            node = document.getElementsByTagName("input").item(0);
            if (null != node) {
                inputList = getList(node);
            } else {
                throw new XmlParseException("the tag of <input> following <rule> can't be empty");
            }

            node = document.getElementsByTagName("back").item(0);
            if (null != node) {
                backList = getList(node);
            } else {
                if (mode == PlatformName.IOS)
                    throw new XmlParseException("the tag of <back> following <rule> can't be empty");
            }

            node = document.getElementsByTagName("trigger").item(0);
            triggerList = getList(node);

            node = document.getElementsByTagName("blackList").item(0);
            blackList = getList(node);

            node = document.getElementsByTagName("notback").item(0);
            notBackList = getList(node);

            node = document.getElementsByTagName("identify-special").item(0);
            identifySpecialList = getList(node);

            config.setScreenshot(screenshot);
            config.setMode(mode);
            config.setRunMode(runMode);
            config.setReverse(reverse);
            config.setDepth(depth);
            config.setFilter(filter);
            config.setRunServer(runServer);
            config.setPort(port);
            config.setDuration(duration);
            config.setInterval(interval);
            config.setTimeout(timeout);
            config.setAllowSameWinTimes(allowSameWinTimes);
            config.setHost(host);
            config.setApp(app);
            config.setUdid(udid);
            config.setTips(tips);
            config.setNotBackList(notBackList);
            config.setLogCmd(logCmd);
            config.setBundleId(bundleId);
            config.setAppPackage(appPackage);
            config.setGuideFlow(guideFlow);
            config.setIdentifyDefault(identifyDefault);
            config.setClickList(clickList);
            config.setInputList(inputList);
            config.setBackList(backList);
            config.setTriggerList(triggerList);
            config.setBlackList(blackList);
            config.setIdentifySpecialList(identifySpecialList);
            config.setCapabilityMap(capabilityMap);

            return config;

        } catch (ParserConfigurationException e) {
            Log.logError(e.fillInStackTrace());
        } catch (SAXException e) {
            Log.logError(e.fillInStackTrace());
        } catch (IOException e) {
            Log.logError(e.fillInStackTrace());
        } catch (XmlParseException e) {
            Log.logError(e.fillInStackTrace());
        }
        return null;
    }

    private List<String> getList(Node node) {
        List<String> list = null;
        if (null != node && !node.getTextContent().isEmpty()) {
            list = new ArrayList<>();
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                    list.add(nodeList.item(i).getTextContent().trim());
            }
        }
        return list;
    }
    
    public void printNode(Node node){
    	System.out.println("printNode------");
    	NodeList tmpNodeList;
    	if (null != node) {
    		tmpNodeList = node.getChildNodes();
            for (int i = 0; i < tmpNodeList.getLength(); i++) {
                if (tmpNodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                    System.out.println(tmpNodeList.item(i).getNodeName().trim() 
                    		+ ": "+tmpNodeList.item(i).getTextContent().trim());
                    
            }
        } 
    }
    
}