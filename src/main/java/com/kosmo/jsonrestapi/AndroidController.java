package com.kosmo.jsonrestapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mybatis.BoardDTO;
import mybatis.IAndroidDAO;
import mybatis.IBoardDAO;
import mybatis.MemberVO;
import mybatis.ParameterDTO;

@Controller
public class AndroidController {
	
	//Mybatis 사용을 위한 자동 주입
	@Autowired
	private SqlSession sqlSession;
		
	/*
	 회원리스트가져오기(json객체형태)
	 Map 컬렉션을 통해 JSON객체 형태로 출력한다.
	 */
	@RequestMapping("/android/memberObject.do") //요청명에 대한 매핑
	@ResponseBody //컬렉션에 저장된 데이터를 JSON형태로 출력
	public Map<String, String> modify() {
		
		ArrayList<MemberVO> record = sqlSession.getMapper(IAndroidDAO.class)
				.memberList();
		
		Map<String, String> map = new HashMap<String, String>();
		for(MemberVO vo : record) {
			map.put("id", vo.getId());
			map.put("pass", vo.getPass());
			map.put("name", vo.getName());
			map.put("regidate", vo.getRegidate().toString());
		}
		return map;
	}
	
}
