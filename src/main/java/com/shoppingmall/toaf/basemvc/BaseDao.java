package com.shoppingmall.toaf.basemvc;

import java.util.List;
import javax.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.shoppingmall.toaf.object.DataMap;

/**
 * <PRE>
 * 기  능	: Spring의 MyBatis 연동 지원 공통 parent DAO 클래스
 * 파일명	: BaseDao.java
 * 패키지	: com.ttmsoft.toaf.basemvc
 * 설  명	: 
 * 변경이력	: 
 * 2015 .07 .20.	[sgchoi] - 최초작성
 * </PRE>
 * 
 * @param <D> - VO, DataMap 클래스
 */
@Repository
public class BaseDao <D> extends SqlSessionDaoSupport {

	/*
	 * @Resource (name = "sqlSession") public void setSqlSessionFactory
	 * (SqlSessionFactory sqlSession) { super.setSqlSessionFactory(sqlSession); }
	 */
	
	@Resource (name="postgresqlSession")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	
	/**
	 * <PRE>
	 * 기  능	: 입력 처리 SQL mapping 을 실행한다.
	 * 기능명	: insertQuery
	 * 설  명	: Mybatis 입력 처리 SQL mapping 을 실행한다.
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 입력 처리 SQL mapping 쿼리 ID
	 * @param vo vo - 입력 처리 SQL mapping 입력 데이터를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @return DBMS가 지원하는 경우 insert 적용 결과 count
	 */
	public int insertQuery (String queryId, D vo) {
		return getSqlSession().insert(queryId, vo);
	}

	public int insertQuery (DataMap map) {
		return getSqlSession().insert((String) map.get("sqlid"), map);
	}

	/**
	 * <PRE>
	 * 기  능	: 수정 처리 SQL mapping 을 실행한다.
	 * 기능명	: updateQuery
	 * 설  명	: 수정 처리 SQL mapping 을 실행한다.
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 수정 처리 SQL mapping 쿼리 ID
	 * @param vo - 수정 처리 SQL mapping 입력 데이터(key 조건 및 변경 데이터)를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @return DBMS가 지원하는 경우 update 적용 결과 count
	 */
	public int updateQuery (String queryId, D vo) {
		return getSqlSession().update(queryId, vo);
	}

	public int updateQuery (DataMap map) {
		return getSqlSession().update((String) map.get("sqlid"), map);
	}

	/**
	 * <PRE>
	 * 기  능	: 삭제 처리 SQL mapping 을 실행한다.
	 * 기능명	: deleteQuery
	 * 설  명	: 삭제 처리 SQL mapping 을 실행한다.
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 삭제 처리 SQL mapping 쿼리 ID
	 * @param vo - 삭제 처리 SQL mapping 입력 데이터(일반적으로 key 조건)를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @return DBMS가 지원하는 경우 delete 적용 결과 count
	 */
	public int deleteQuery (String queryId, D vo) {
		return getSqlSession().delete(queryId, vo);
	}

	public int deleteQuery (DataMap map) {
		return getSqlSession().delete((String) map.get("sqlid"), map);
	}

	/**
	 * <PRE>
	 * 기  능	: 조회 건수를 얻는다.
	 * 기능명	: countQuery
	 * 설  명	: 조회 건수를 얻는다.
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 조회 건수 처리 SQL mapping 쿼리 ID
	 * @param vo - 조회 건수 처리 SQL mapping 입력 데이터(key)를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @return 조회건수
	 */
	public int countQuery (String queryId, D vo) {
		return (Integer) getSqlSession().selectOne(queryId, vo);
	}

	public int countQuery (DataMap map) {
		return (Integer) getSqlSession().selectOne((String) map.get("sqlid"), map);
	}

	/**
	 * <PRE>
	 * 기  능	: 단건조회 처리 SQL mapping 을 실행한다.
	 * 기능명	: selectQuery
	 * 설  명	: 단건조회 처리 SQL mapping 을 실행한다.
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 단건 조회 처리 SQL mapping 쿼리 ID
	 * @param vo - 단건 조회 처리 SQL mapping 입력 데이터(key)를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @return 결과 객체 - SQL mapping 파일에서 지정한 resultClass/resultMap 에 의한 단일 결과 객체(보통 VO 또는 Map)
	 */
	@SuppressWarnings ("unchecked")
	public D selectQuery (String queryId, D vo) {
		return (D) getSqlSession().selectOne(queryId, vo);
	}

	@SuppressWarnings ({ "unchecked" })
	public D selectQuery (DataMap map) {
		return (D) getSqlSession().selectOne((String) map.get("sqlid"), map);
	}

	/**
	 * <PRE>
	 * 기  능	: 리스트 조회 처리 SQL mapping 을 실행한다.
	 * 기능명	: dolistQuery
	 * 설  명	: 리스트 조회 처리 SQL mapping 을 실행한다.
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 리스트 조회 처리 SQL mapping 쿼리 ID
	 * @param vo - 리스트 조회 처리 SQL mapping 입력 데이터(조회 조건)를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @return 결과 List 객체 - SQL mapping 파일에서 지정한 resultClass/resultMap 에 의한 결과 객체(보통 VO 또는 Map)의 List
	 */
	public List<D> dolistQuery (String queryId, D vo) {
		return getSqlSession().selectList(queryId, vo);
	}

	public List<D> dolistQuery (DataMap map) {
		return getSqlSession().selectList((String) map.get("sqlid"), map);
	}

	/**
	 * <PRE>
	 * 기  능	: 부분 범위 리스트 조회 처리 SQL mapping 을 실행한다.
	 * 기능명	: dopageQuery
	 * 설  명	: 부분 범위 - pageIndex 와 pageSize 기반으로 현재 부분 범위 조회를 위한 skipResults, maxResults 를 계산하여 ibatis 호출
	 * 변경이력	: 
	 * 2015. 7. 20.	[PUDDING] - 최초작성
	 * </PRE>
	 * 
	 * @param queryId - 리스트 조회 처리 SQL mapping 쿼리 ID
	 * @param vo - 리스트 조회 처리 SQL mapping 입력 데이터(조회 조건)를 세팅한 파라메터 객체(보통 VO 또는 Map)
	 * @param page - 현재 페이지 번호
	 * @param size - 한 페이지 조회 수(pageSize)
	 * @return 부분 범위 결과 List 객체 - SQL mapping 파일에서 지정한 resultClass/resultMap 에 의한 부분 범위 결과 객체(보통 VO 또는 Map) List
	 */
	public List<D> dopageQuery (String queryId, D vo, int page, int size) {
		int skip = page * size;

		RowBounds rowBounds = new RowBounds(skip, size);
		return getSqlSession().selectList(queryId, vo, rowBounds);
	}

	public List<D> dopageQuery (DataMap map, int page, int size) {
		int skip = page * size;

		RowBounds rowBounds = new RowBounds(skip, size);
		return getSqlSession().selectList((String) map.get("sqlid"), map, rowBounds);
	}
	

}
