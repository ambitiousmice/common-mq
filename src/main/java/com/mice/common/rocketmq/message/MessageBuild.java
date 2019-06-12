/**
 * 
 */
package com.mice.common.rocketmq.message;

import java.nio.charset.Charset;

import org.apache.rocketmq.common.message.Message;

/**
 * @description:
 * @author mice
 * @date 2019/6/11
*/
public class MessageBuild {

	public static Message build(String topic, String tags, String data){
		return new Message(topic, tags, data.getBytes(Charset.forName("UTF-8")));
	}

}
