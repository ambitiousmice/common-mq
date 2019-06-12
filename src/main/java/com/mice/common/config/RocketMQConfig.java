/**
 * 
 */
package com.mice.common.config;

import com.mice.common.rocketmq.RocketmqTemplate;
import com.mice.common.rocketmq.consumer.IMQConsumer;
import com.mice.common.rocketmq.mqenum.Groups;
import com.mice.common.rocketmq.producer.IMQProducer;
import com.mice.common.rocketmq.producer.impl.DefaultProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @description:服务器自定义RocketMQ
 * @author mice
 * @date 2019/6/11
*/
@Configuration
@EnableAutoConfiguration
public class RocketMQConfig {

	@Value("${apache.rocketmq.namesrvAddr}")
	private String nameServer;
	@Value("${eureka.instance.hostname}")
	private String host;
	@Value("${spring.application.name}")
	private String name;
	@Value("${server.port}")
	private String port;
	// 消费者列表
	@Autowired(required = false)
	private List<IMQConsumer> consumerHandlers;
	
	@Bean
	RocketmqTemplate rocketmqTemplate() {
		// 生产者
		IMQProducer pushMqProducer = new DefaultProducer(nameServer, Groups.LOG);
		// 服务唯一名称
		String instanceName = name + ":" + host + ":" + port;
		return new RocketmqTemplate(nameServer, pushMqProducer, consumerHandlers, instanceName);
	}
}
