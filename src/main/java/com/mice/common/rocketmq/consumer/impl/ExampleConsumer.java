/**
 * 
 */
package com.mice.common.rocketmq.consumer.impl;

import com.mice.common.rocketmq.consumer.IMQConsumer;
import com.mice.common.rocketmq.mqenum.Groups;
import com.mice.common.rocketmq.mqenum.Tags;
import com.mice.common.rocketmq.mqenum.Topics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 消费者例
 */
@Service
@Slf4j
public class ExampleConsumer implements IMQConsumer {

	@Override
	public Groups getGroup() {
		return Groups.LOG;
	}

	@Override
	public Topics getTopic() {
		return Topics.LOG;
	}

	@Override
	public Tags getTag() {
		return Tags.LOG_EXAMPLE;
	}

	@Override
	public void handler(String msg) throws Exception {
		log.info(msg);
	}

}
