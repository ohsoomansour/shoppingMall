package com.shoppingmall.basemvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/* 방법1. main class는 모든 package를 포함할 수 있는 곳에 Springboot Main Class가 위치해야 하
   -> 
 * 방법2.@ComponentScan을 사용하여 
 * */
public abstract class BaseSvc <D> {

	protected Logger 		logger	= LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected BaseDao<D> 	dao;

}