package com.onecoderspace.base;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=BaseApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderService2Test {

	@Category(UnifiedOrderTaskTests.class)
	@Test
	public void testInsert(){
		System.err.println("OrderService2 test insert");
	}

	@Test
	public void testQuery(){
		System.err.println("OrderService2 test query");
	}

	@Category(UnifiedOrderTaskTests.class)
	@Test
	public void testQuery2(){
		System.err.println("OrderService2 test query 2");
	}
}
