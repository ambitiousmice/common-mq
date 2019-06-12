package com.mice.common.rocketmq.producer.impl;

import com.mice.common.rocketmq.message.MessageBuild;
import com.mice.common.rocketmq.mqenum.Groups;
import com.mice.common.rocketmq.mqenum.Tags;
import com.mice.common.rocketmq.mqenum.Topics;
import com.mice.common.rocketmq.producer.IMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

@Slf4j
public class DefaultProducer implements IMQProducer {

	// NameServer地址
	private String namesrvAddr;
	// 生产者组
	private String producerGroup;
	// MQ默认的生产者
	private DefaultMQProducer defaultMQProducer;
	
	public DefaultProducer(String namesrvAddr, Groups producerGroup) {
		this.namesrvAddr = namesrvAddr;
		this.producerGroup = producerGroup.getProducerGroup();
		init();

	}

	private void init(){
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroup);
		defaultMQProducer.setNamesrvAddr(namesrvAddr);
		try {
			defaultMQProducer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		this.defaultMQProducer = defaultMQProducer;
	}
	
	@Override
	public boolean send(Topics topic, Tags tags, String msg) throws Exception {
		Message data = MessageBuild.build(topic.name(), tags.name(), msg);
		if (log.isDebugEnabled()) {
			log.debug(msg);
		}
		SendResult result = defaultMQProducer.send(data);
		if (log.isDebugEnabled()) {
			log.debug(result.toString());
		}
		if (result.getSendStatus() == SendStatus.SEND_OK) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String getGroup() {
		return producerGroup;
	}
}
