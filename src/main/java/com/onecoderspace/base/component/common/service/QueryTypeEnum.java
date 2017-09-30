package com.onecoderspace.base.component.common.service;

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
