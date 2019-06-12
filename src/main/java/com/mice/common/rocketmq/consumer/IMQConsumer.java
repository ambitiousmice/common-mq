/**
 * 
 */
package com.mice.common.rocketmq.consumer;


import com.mice.common.rocketmq.mqenum.Groups;
import com.mice.common.rocketmq.mqenum.Tags;
import com.mice.common.rocketmq.mqenum.Topics;

/**
 * @description:
 * @author mice
 * @date 2019/6/11
*/
public interface IMQConsumer {

	/**
	 * 获取消费组
	 * @return
	 */
	Groups getGroup();
	
	/**
	 * 获取消费标题
	 * @return
	 */
	Topics getTopic();
	
	/**
	 * 获取消费标签
	 * @return
	 */
	Tags getTag();
	
	/**
	 * 消费队列的消息
	 * @param msg
	 * @throws Exception
	 */
	void handler(String msg) throws Exception;
}
