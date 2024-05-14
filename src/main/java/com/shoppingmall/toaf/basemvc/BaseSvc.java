package com.shoppingmall.toaf.basemvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseSvc <D> {

	protected Logger		logger	= LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected BaseDao<D>	dao;

}
