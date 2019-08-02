/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.common.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author jeeplus
 * @version 2016-01-15
 */
@Service
@Lazy(false)
public class IdGen implements IdGenerator, SessionIdGenerator {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Encodes.encodeBase62(randomBytes);
	}
	
	/**
	 * Activiti ID 生成
	 */
	@Override
	public String getNextId() {
		return IdGen.uuid();
	}

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}


	//信息编码生成
	public static String getInfoCode(String prefix) {
		StringBuilder buf=null;
		if(StringUtils.isNotBlank(prefix)){
			buf = new StringBuilder(prefix);
			buf.append("-");
		}else{
			buf = new StringBuilder("");
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssS");
		String str = formatter.format(new Date());
		buf.append(str);
		return buf.toString();
	}
	//生成订单编号11位
	public static String getOrderCode() {

		int hashCodev = UUID.randomUUID().toString().hashCode();
		System.out.println(UUID.randomUUID().toString());
		if(hashCodev < 0){
			//有可能是负数
			hashCodev = -hashCodev;
		}
		//"%011d"的意思：0代表不足位数的补0，这样可以确保相同的位数，11是位数也就是要得到到的字符串长度是11，d代表数字。
		//return machineId + String.format("%010d", hashCodev);
		System.out.println("^^^^^^^^^^^^^^^^"+hashCodev);
		return String.format("%011d", hashCodev);

	}
	public static void main(String[] args) {
		System.out.println(IdGen.uuid());
		System.out.println(IdGen.uuid().length());
		System.out.println(new IdGen().getNextId());
		for (int i=0; i<1000; i++){
			System.out.println(IdGen.randomLong() + "  " + IdGen.randomBase62(5));
		}
	}

}
