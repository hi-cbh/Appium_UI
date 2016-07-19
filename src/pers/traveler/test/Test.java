package pers.traveler.test;


public class Test {
	//static Config config;
	static String path = "click>>#com#chinamobile.contacts.im:id/skip_btn1";
	public static void main(String[] args) {
		String tmp = path.replaceAll("#com#", "what");
		System.out.println(tmp);
	}

}
