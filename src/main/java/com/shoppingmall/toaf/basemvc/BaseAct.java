package com.shoppingmall.toaf.basemvc;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;

public class BaseAct {

	protected Logger	logger	= LoggerFactory.getLogger(this.getClass());

	protected BaseAct () {
		super();
	}

	protected MessageSource	messageSource;

	@Resource (name = "messageSource")
	public void setMessageSource (MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
