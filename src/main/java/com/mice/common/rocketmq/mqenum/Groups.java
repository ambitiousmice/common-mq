/**
 * 
 */
package com.mice.common.rocketmq.mqenum;

/**
 * @description: 分组信息
 * @author mice
 * @date 2019/6/11
*/
public enum Groups {

	LOG("LOG_CONSUMER", "LOG_PRODUCER"),

	;

	// 消费者组
	private String consumer;
	// 生产者组
	private String producer;

	private Groups(String consumer, String producer) {
		this.consumer = consumer;
		this.producer = producer;
	}

	public String getConsumerGroup() {
		return consumer;
	}

	public String getProducerGroup() {
		return producer;
	}

}
