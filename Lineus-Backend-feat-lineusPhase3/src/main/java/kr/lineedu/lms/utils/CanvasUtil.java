package kr.lineedu.lms.utils;

import java.util.Random;

public class CanvasUtil {

	static String chararray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	static String numberarray = "0123456789012345678901234567890123456789";

	public static String getRandomUuid(int len) {
		Random random = new Random(System.currentTimeMillis());
		String ret = "";
		for (int i = 0; i < len; i++) {
			ret += chararray.charAt(random.nextInt(chararray.length()));
		}
		return ret;
	}

	public static String getRandomNumber(int len) {
		Random random = new Random(System.currentTimeMillis());
		String ret = "";
		for (int i = 0; i < len; i++) {
			ret += numberarray.charAt(random.nextInt(numberarray.length()));
		}
		return ret;
	}
}



