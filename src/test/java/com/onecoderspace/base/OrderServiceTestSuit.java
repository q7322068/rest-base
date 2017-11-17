package com.onecoderspace.base;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by wenkuiyang on 2017/11/17.
 */
@RunWith(Categories.class)
@Suite.SuiteClasses({OrderServiceTest.class,OrderService2Test.class})
//@Categories.IncludeCategory(UnifiedOrderTaskTests.class)
@ExcludeCategory(UnifiedOrderTaskTests.class)
public class OrderServiceTestSuit {

}
