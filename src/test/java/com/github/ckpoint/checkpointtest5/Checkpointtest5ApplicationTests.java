package com.github.ckpoint.checkpointtest5;

import com.github.ckpoint.spring5.model.BaseModel;
import com.github.ckpoint.spring5.model.order.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
public class Checkpointtest5ApplicationTests {

	@Test
	public void contextLoads() {
		log.info(BaseModel.class.getSuperclass().getName());
		log.info(OrderModel.class.getSuperclass().getName());
	}

}
