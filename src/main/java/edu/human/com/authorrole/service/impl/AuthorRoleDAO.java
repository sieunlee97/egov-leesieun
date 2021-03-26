package edu.human.com.authorrole.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.human.com.authorrole.service.AuthorRoleVO;
import edu.human.com.common.EgovComAbstractMapper;
import edu.human.com.util.PageVO;

@Repository
public class AuthorRoleDAO extends EgovComAbstractMapper{
	public List<AuthorRoleVO> selectAuthorRole(PageVO pageVO) throws Exception {
		List<AuthorRoleVO> authorRoleList = selectList("authorroleMapper.selectAuthorRole", pageVO);
		return authorRoleList;
	}

}
