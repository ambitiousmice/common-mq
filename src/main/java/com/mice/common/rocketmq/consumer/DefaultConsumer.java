/**
 * 
 */
package com.mice.common.rocketmq.consumer;

import com.mice.common.rocketmq.mqenum.Groups;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @description: Group分组下默认的消费者
 * @author mice
 * @date 2019/6/11
*/
@Slf4j
public class DefaultConsumer {

	// NameServer地址
	private String namesrvAddr;
	// 消费者唯一名字
	private String instanceName;
	// 消费者生产组
	private DefaultMQPushConsumer consumer;
	// 消费者回调列表(key=话题Topic,value=标签列表)
	private Map<String, List<IMQConsumer>> mqConsumers = new HashMap<String, List<IMQConsumer>>();

	public DefaultConsumer(String nameServer, Groups groups, String instanceName) throws Exception {
		this.namesrvAddr = nameServer;
		this.consumer = new DefaultMQPushConsumer(groups.getConsumerGroup());
		this.instanceName = instanceName;
	}

	/**
	 * 启动消费组
	 * 
	 * @throws MQClientException
	 */
	public void start() throws MQClientException {
		for (String topic : mqConsumers.keySet()) {
			// 设置消费标签
			consumer.subscribe(topic, getTags(mqConsumers.get(topic)));
		}

		// 设置唯一名称
		consumer.setInstanceName(instanceName);
		// 指定NameServer地址，多个地址以 ; 隔开
		consumer.setNamesrvAddr(namesrvAddr);
		// 如果非第一次启动，那么按照上次消费的位置继续消费
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		// 注册消费回调
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
				try {
					for (MessageExt messageExt : list) {
						if (log.isDebugEnabled()) {
							log.debug(messageExt.toString());
						}
						String messageBody = new String(messageExt.getBody(), "utf-8");
						// 获取指定话题下的所有消费者
						List<IMQConsumer> dmgcinfos = mqConsumers.get(messageExt.getTopic());
						// 检查是否有消费者
						if (dmgcinfos != null && dmgcinfos.size() >= 1) {
							for (IMQConsumer dmgcinfo : dmgcinfos) {
								// 判断消息是否为指定标签
								if (dmgcinfo.getTag().name().equals(messageExt.getTags())) {
									dmgcinfo.handler(messageBody);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return ConsumeConcurrentlyStatus.RECONSUME_LATER; // 稍后再试
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 消费成功
			}
		});
		consumer.start();
	}

	/**
	 * 添加话题和标签到指定的消费组
	 * 
	 * @param topic
	 * @param tags
	 * @throws MQClientException
	 */
	public void putValueToConsumer(IMQConsumer handler) throws MQClientException {
		// 添加到消费者回调列表
		if (mqConsumers.containsKey(handler.getTopic().name())) {
			mqConsumers.get(handler.getTopic().name()).add(handler);
		} else {
			List<IMQConsumer> list = new ArrayList<IMQConsumer>();
			list.add(handler);
			mqConsumers.put(handler.getTopic().name(), list);
		}
	}

	/**
	 * 整合标签
	 * 
	 * @param topic
	 * @return
	 */
	private String getTags(List<IMQConsumer> list) {
		if (list == null || list.size() <= 0) {
			return "";
		}
		StringBuilder tags = new StringBuilder();
		int size = list.size();
		int i = 0;
		for (IMQConsumer t : list) {
			i++;
			if (i < size) {
				tags.append(t.getTag()).append("||");
			} else {
				tags.append(t.getTag());
			}
		}
		return tags.toString();
	}
}
