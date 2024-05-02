package com.shoppingmall.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class AdminService {

}
