package com.mable.springbootproducer.constants;

public class ConstantsSetting {

	
	/**dao接口的路径**/
	public static final String MAPPER_PACKAGE_NAME = "com.mable.springbootproducer.mapper";
	/**sql所在的xml路径**/								
	public static final String RESOURCES_PACKAGE_PATH = "classpath:com/mable/springbootproducer/mapping/*.xml";
	
	public static final String RABBIT_EXCHANGE = "order-exchange";
	public static final String RABBIT_ROUTING_KEY = "order.abc";
}
