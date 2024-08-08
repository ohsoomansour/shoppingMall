package shoppingMall;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.ShoppingMallApp;

/**
 * @junit사용법 : test 메서드 오른 쪽 마우스 -> jUnit Test 
 * @에러1.
 * [main] INFO org.springframework.test.context.support.AnnotationConfigContextLoaderUtils 
 *         Could not detect default configuration classes for test class
 *  - 이유 : @SpringBootApplication가 있는 클래스에서 실행하지 않아서 
 *  - 해결:@SpringBootApplication class를 명시적으로 알려줘야한다. 
 *        @ContextConfiguration(classes = ShoppingMallApp.class)
 * @에러2.
 * org.opentest4j.AssertionFailedError: Not yet implemented     #### 주목!! #####
  	at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:38)
  	at org.junit.jupiter.api.Assertions.fail(Assertions.java:135)
  	at shoppingMall.dbconn.test(dbconn.java:45)
  	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
  	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
  	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:727)
  	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
  	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
  
  - 이유 //fail("Not yet implemented"); 를 사용하면 무조건 에러를 발생한다. 

 * */


@SpringBootTest
@ContextConfiguration(classes = ShoppingMallApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
class dbconn {
		@Autowired
		private JdbcTemplate jdbcTemplate;
	
		@Test
		void test() {
				String sql = "SELECT 1";
			  Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
			  System.out.println("result:" +  result);
			  assertNotNull(result);
			  assertEquals(1, result.intValue(), "Database connection failed or returned unexpected result.");
				//fail("Not yet implemented");
		}

}
