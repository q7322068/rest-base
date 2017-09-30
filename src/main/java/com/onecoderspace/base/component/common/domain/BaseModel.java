package com.onecoderspace.base.component.common.domain;

import java.io.Serializable;

public interface BaseModel<ID extends Serializable> extends Serializable{

	ID getId();
	
}
