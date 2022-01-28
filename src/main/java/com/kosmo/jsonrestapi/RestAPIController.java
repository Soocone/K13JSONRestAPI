package com.kosmo.jsonrestapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mybatis.BoardDTO;
import mybatis.IBoardDAO;
import mybatis.ParameterDTO;

@Controller
public class RestAPIController {
	
	//Mybatis 사용을 위한 자동 주입
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("/restapi/boardList.do") //요청명에 대한 매핑
	@ResponseBody //컬렉션에 저장된 데이터를 JSON형태로 출력
	public ArrayList<BoardDTO> boardList(HttpServletRequest req) {
		
		//파라미터 저장용 DTO 객체
		ParameterDTO parameterDTO = new ParameterDTO();
		//검색 필드 저장
		parameterDTO.setSearchField(req.getParameter("searchField"));
		//검색어 저장
		ArrayList<String> searchLists = null;
		if(req.getParameter("searchTxt")!=null) {
			/*
			 검색어가 2개 이상인 경우 스페이스로 구분하여 전송하면 split을
			 통해 문자열을 분리한 후 List에 하나씩 추가한다.
			 */
			searchLists = new ArrayList<String>();
			String[] sTxtArray = req.getParameter("searchTxt").split(" ");
			for(String str : sTxtArray) {
				searchLists.add(str);
			}
		}
		parameterDTO.setSearchTxt(searchLists);
		System.out.println("검색어: "+searchLists);
		
		/*
		 컨트롤러에서 Service 객체 역할을 하는 interface에 정의된 추상메서드를
		 호출한다. 그러면 mapper에 정의된 쿼리문이 실행되는 형식으로 동작한다.
		 동작방식] 컨트롤러에서 메서드 호출 -> interface의 추상메서드 호출
		 	-> namespace에 해당 interface를 namespace로 지정된 매퍼 선택
		 	-> 추상메서드와 동일한 이름의 id 속성을 가진 엘리먼트 선택
		 	-> 쿼리문 실행 및 결과 반환
		 */
		int totalRecordCount = sqlSession.getMapper(IBoardDAO.class).getTotalCount(parameterDTO);
		
		//페이지 처리를 위한 설정
		int pageSize = 10; //한 페이지당 출력할 게시물의 개수
		//페이지 번호를 출력하지 않으므로 여기서는 필요 없음
		//int blockPage = 2; //한 블럭당 출력할 페이지 번호의 개수
		
		//전체 페이지수 계산
		int totalPage = (int)Math.ceil((double)totalRecordCount/pageSize);
		
		//현재페이지 번호 설정
		/*
		 방명록URL?nowPage=		-> 이경우 페이지 번호는 빈값
		 방명록URL?nowPage=10	-> 10으로 설정
		 방명록URL				-> null로 판단
		 */
		//페이지 번호가 null이거나 빈값인 경우 1페이지로 설정한다.
		int nowPage = (req.getParameter("nowPage")==null || req.getParameter("nowPage").equals(""))
				? 1 : Integer.parseInt(req.getParameter("nowPage"));
		
		//해당 페이지에 출력할 게시물의 구간을 계산한다.
		int start = (nowPage-1) * pageSize + 1;
		int end = nowPage * pageSize;
		
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		
		/*
		 서비스 역할의 인터페이스의 추상메서드를 호출하면 mapper가 동작됨
		 전달된 파라미터는 #{param1}과 같이 순서대로 사용한다.
		 */
		//현재 페이지에 해당하는 레코드 추출
		ArrayList<BoardDTO> lists = 
				sqlSession.getMapper(IBoardDAO.class).listPage(parameterDTO);
		
		//내용에 대해 줄바꿈 처리
		for(BoardDTO dto : lists) {
			String temp = dto.getContent().replace("\r\n", "<br>");
			dto.setContent(temp);
		}
		
		//List객체를 반환한다. 따라서 JSON 배열 형태로 출력된다.
		return lists;
	}

	/*
	 게시물 상세보기는 게시물 하나를 출력하므로 Map 컬렉션을 통해
	 JSON객체 형태로 출력한다.
	 */
	@RequestMapping("/restapi/boardView.do") //요청명에 대한 매핑
	@ResponseBody //컬렉션에 저장된 데이터를 JSON형태로 출력
	public Map<String, String> modify(ParameterDTO parameterDTO) {
		
		ArrayList<BoardDTO> record = sqlSession.getMapper(IBoardDAO.class)
				.view(parameterDTO);
		
		Map<String, String> map = new HashMap<String, String>();
		for(BoardDTO dto : record) {
			map.put("num", dto.getNum());
			map.put("title", dto.getTitle());
			map.put("content", dto.getContent());
			map.put("id", dto.getId());
			map.put("postdate", dto.getPostdate().toString());
			map.put("visitcount", dto.getVisitcount());
		}
		return map;
	}
}
