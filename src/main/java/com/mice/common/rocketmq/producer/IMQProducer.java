package com.mice.common.rocketmq.producer;


import com.mice.common.rocketmq.mqenum.Tags;
import com.mice.common.rocketmq.mqenum.Topics;

/**
 * @description:
 * @author mice
 * @date 2019/6/11
*/
public interface IMQProducer {

	String getGroup();
	
	boolean send(Topics topic, Tags tags, String msg) throws Exception;
}
