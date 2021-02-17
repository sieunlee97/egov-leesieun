package edu.human.com.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * EgovComAbstractMapper클래스는 sqlSession템플릿을 DAO클래스에서 직접 호출하지 않고,
 * 전자정부에서 제공하는 EgovAbstractMapper(마이바티스용)클래스를 상속받아서 생성한 
 * 개발자(개발사) 클래스의 뭐리 템플릿을 재정의해서 사용한다.
 * 
 * 추상클래스? 자동차(소형, 중형, 대형)
 * 1. new 키워드로 인스턴스 실행 클래스를 만들 수 없다.
 * 2. 상속을 통해서 클래스의 메소드를 클래스의 메소드를 실행 가능하다.
 * 3. 오버라이드해서 전자정부에서 제공한 EgovAbstractMapper 추상클래스에서 정의된 명세를 
 * 	 아래 클래스 재정의(오버라이딩)해서 메소드를 구현한다.
 * 추상클래스 만드는 목적 : 멤버변수 또는 멤버메소드를 규격화한다. 전자정부표준을 준수하였는지 인증받기 위해서
 * @author 이시은
 *
 */
public abstract class EgovComAbstractMapper extends EgovAbstractMapper {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Resource(name="egov.sqlSession")
	
	public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
		super.setSqlSessionFactory(sqlSession);
	}
	
	
	@Override
	public int delete(String queryId) {
		return getSqlSession().delete(queryId);
	}
	@Override
	public int delete(String queryId, Object parameterObject) {
		return getSqlSession().delete(queryId, parameterObject);
	}
	@Override
	public int insert(String queryId) {
		return getSqlSession().insert(queryId);
	}
	@Override
	public int insert(String queryId, Object parameterObject) {
		return getSqlSession().insert(queryId, parameterObject);
	}
	@Override
	public <E> List<E> selectList(String queryId, Object parameterObject, RowBounds rowBounds) {
		return getSqlSession().selectList(queryId, parameterObject, rowBounds);
	}
	@Override
	public <E> List<E> selectList(String queryId, Object parameterObject) {
		return getSqlSession().selectList(queryId, parameterObject);
	}
	@Override
	public <E> List<E> selectList(String queryId) {
		return  getSqlSession().selectList(queryId);
	}
	@Override
	public <T> T selectOne(String queryId) {
		return getSqlSession().selectOne(queryId);
	}
	@Override
	public <T> T selectOne(String queryId, Object parameterObject) {
		return getSqlSession().selectOne(queryId, parameterObject);
	}
	@Override
	public int update(String queryId) {
		return getSqlSession().update(queryId);
	}
	@Override
	public int update(String queryId, Object parameterObject) {
		return getSqlSession().update(queryId, parameterObject);
	}
	
	/**
	 * 페이징범위 계산 : pageIndex(선택한페이지)와 pageSize(= limit 한페이지당보여줄개수) 두 변수값을 매개변수로 받아서 
	 * 쿼리에서 시작 인덱스 번호 구하기 : offset = (pageIndex-1)*pageSize;
	 * 1페이지일때 시작 offset = (1-1)x10=0  [테이블에서는 1번째 레코드]
	 * 2페이지일때 시작 offset = (2-1)x10=10 [테이블에서는 11번째 레코드]
	 * 계산결과1 : offset = pageIndex X pageSize = 선택한 페이지까지 검색된 개수
	 * 			= 화면에 출력할 내용 중 시작할 번호 ( ex. 3페이지 클릭 - 3번*10개 =30번, 인덱스는 0번부터이기 때문에 실제 번호는 31번 )
	 * 계산결과2 : maxResults = (선택한페이지*한페이지당보여줄개수)+한페이지당보여줄개수
	 * 			= 화면에 출력할 내용 중 끝 번호 ( ex. 30번 + 10개 = 40번 )
	 * 
	 * queryStartNo = queryPerPageNum*(this.page-1); // 개발자가 추가한 계산식
	 */
	@Override
	public List<?> listWithPaging(String queryId, Object parameterObject, int pageIndex, int pageSize) {
		int offset = pageIndex * pageSize; 
		RowBounds rowBounds = new RowBounds(offset, pageSize); //(시작인덱스번호, 꺼내올개수)
		return getSqlSession().selectList(queryId, parameterObject, rowBounds);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String queryId, Object parameterObject, String mapKey) {
		// 공통코드를 위한 맵타입을 반환하는 sqlSesseion템플릿 사용(아래)
		return getSqlSession().selectMap(queryId, parameterObject, mapKey);
	}


	@Override
	public <K, V> Map<K, V> selectMap(String queryId, String mapKey) {
		// 그룹코드는 key로, 이름은 value로 맵자료형으로 반환하는 sqlSession템플릿 사용(아래)
		return getSqlSession().selectMap(queryId, mapKey);
	}

	
}
