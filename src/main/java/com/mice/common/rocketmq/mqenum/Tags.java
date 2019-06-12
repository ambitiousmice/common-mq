/**
 * 
 */
package com.mice.common.rocketmq.mqenum;

/**
 * @author 消息标签
 */
public enum Tags {

	// 俱乐部游戏战绩
	CLUB_GAME_RECORD(true),
	LOG_EXAMPLE(true),

	;
	// 消费模式(true=集群消费,false=广播消费)
	private boolean consumerType;

	private Tags(boolean consumerType) {
		this.consumerType = consumerType;
	}

	public boolean isConsumerType() {
		return consumerType;
	}

	public void setConsumerType(boolean consumerType) {
		this.consumerType = consumerType;
	}
}
