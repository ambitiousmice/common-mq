
package com.mice.common.rocketmq;


import com.mice.common.rocketmq.consumer.DefaultConsumer;
import com.mice.common.rocketmq.consumer.IMQConsumer;
import com.mice.common.rocketmq.mqenum.Groups;
import com.mice.common.rocketmq.mqenum.Tags;
import com.mice.common.rocketmq.mqenum.Topics;
import com.mice.common.rocketmq.producer.IMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author mice
 * @date 2019/6/11
*/
@Slf4j
public class RocketmqTemplate {

	// 服务唯一名称
	private String instanceName;
	private String nameServer;
	// 消费者回调列表
	private List<IMQConsumer> consumerHandlers;
	// 生产者信息
	private IMQProducer producerHandlers;
	// 消费者组信息
	private Map<Groups, DefaultConsumer> map = new HashMap<Groups, DefaultConsumer>();

	public RocketmqTemplate(String nameServer, IMQProducer producerHandlers, List<IMQConsumer> consumerHandlers,
                            String instanceName) {
		this.nameServer = nameServer;
		this.producerHandlers = producerHandlers;
		this.consumerHandlers = consumerHandlers;
		this.instanceName = instanceName;
	}

	@PostConstruct
	private void init() throws Exception {
		// 初始化并合并消费者
		if (consumerHandlers != null && consumerHandlers.size() > 0) {
			for (IMQConsumer handler : consumerHandlers) {
				// 非空检查
				if (handler.getGroup() == null || handler.getTopic() == null || handler.getTag() == null) {
					throw new Exception("group || topic || tags can not null");
				}
				DefaultConsumer c = null;
				if (map.containsKey(handler.getGroup())) {
					c = map.get(handler.getGroup());
				} else {
					c = new DefaultConsumer(nameServer, handler.getGroup(), instanceName);
					map.put(handler.getGroup(), c);
				}
				c.putValueToConsumer(handler);
			}
			for (DefaultConsumer defaultConsumer : map.values()) {
				defaultConsumer.start();
			}
		}
	}

	/**
	 * 发送消息到消息队列
	 * 
	 * @param topic Topics定义的主题
	 * @param tags  Tags定义的标签
	 * @param obj   要发送的数据
	 */
	public void sendMsg(Topics topic, Tags tags, String obj) {
		try {
			boolean success = producerHandlers.send(topic, tags, obj);
			log.info("===>topic为 {} 的MQ消息发送结果为: {}",topic,success);
		} catch (MQClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
