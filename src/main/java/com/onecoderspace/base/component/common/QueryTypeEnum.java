package com.onecoderspace.base.component.common;

import io.swagger.annotations.ApiModel;

@ApiModel(value="查询条件支持的过滤方式")
public enum QueryTypeEnum {
	like,
	equal,
	ne,
	lt,
	lte,
	gt,
	gte
}
