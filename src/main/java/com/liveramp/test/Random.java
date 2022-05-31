package com.liveramp.test;

public class Random {
	/**
	 * 获取两数之间的随机值
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static int getRandom(int x, int y) {
		int num = -1;
		//两个数在合法范围内，并不限制输入的数哪个更大一些
		if (x < 0 || y < 0) {
			return num;
		} else {
			int max = Math.max(x, y);
			int min = Math.min(x, y);
			int mid = max - min;//求差
			//产生随机数
			num = (int) (Math.random() * (mid + 1)) + min;
		}
		return num;
	}
}
