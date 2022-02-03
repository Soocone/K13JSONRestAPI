package com.kosmo.jsonrestapi;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		
		//파라미터 저장용 DTO객체
		ParameterDTO parameterDTO = new ParameterDTO();
		//검색 필드 저장
		parameterDTO.setSearchField(req.getParameter("searchField"));
		//검색어 저장
		ArrayList<String> searchLists = null;
		if(req.getParameter("searchTxt")!=null) {
			/*
			검색어가 2개이상인 경우 스페이스로 구분하여 전송하면 split을
			통해 문자열을 분리한 후 List에 하나씩 추가한다. 
			 */
			searchLists = new ArrayList<String>();
			String[] sTxtArray = req.getParameter("searchTxt").split(" ");
			for(String str : sTxtArray) {
				searchLists.add(str);
			}
		}
		parameterDTO.setSearchTxt(searchLists);
		System.out.println("검색어:"+searchLists);

		//개시물 갯수 카운트
		int totalRecordCount =
			sqlSession.getMapper(IBoardDAO.class).getTotalCount(parameterDTO);
		
		//한페이지에 출력할 게시물의 갯수
		int pageSize = 10;
		//int blockPage = 2; //페이지 번호를 출력하지 않으므로 여기서는 필요없음
		//페이지수 계산
		int totalPage = (int)Math.ceil((double)totalRecordCount/pageSize);
		//현제 페이지번호 가져오기
		int nowPage = req.getParameter("nowPage")==null ? 1 :
			Integer.parseInt(req.getParameter("nowPage"));
		//게시물의 구간 계산
		int start = (nowPage-1) * pageSize + 1;
		int end = nowPage * pageSize;
		//DTO에 저장
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		//현제 페이지에 해당하는 레코드 추출
		ArrayList<BoardDTO> lists = sqlSession.getMapper(IBoardDAO.class)
				.listPage(parameterDTO);
		//내용에 대해 줄바꿈 처리		
		for(BoardDTO dto : lists){			 
			String temp = dto.getContent().replace("\r\n","<br/>");
			dto.setContent(temp);			
		}
		//List객체를 반환한다. 따라서 JSON배열 형태로 출력된다.
		return lists;
	}
	
	/*
	게시물 상세보기는 게시물 하나를 출력하므로 Map컬렉션을 통해
	JSON객체 형태로 출력한다. 
	 */
	@RequestMapping("/restapi/boardView.do")
	@ResponseBody
	public Map<String, String> view(HttpServletRequest req, 
			ParameterDTO parameterDTO) {
				
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
	
	//Get방식으로 요청받은후 글쓰기 처리(테스트용)
	@RequestMapping(value="/restapi/boardWrite.do", method=RequestMethod.GET)
	@ResponseBody 
	//커멘드 객체를 통해 폼값을 한번에 전달받는다. 
	public Map<String, String> writeGet(BoardDTO boardDTO) {
		
		System.out.println("title="+ boardDTO.getTitle());
		
		//Mapper의 write 메서드 호출. <insert 엘리먼트의 경우 항상 0, 1을 반환한다. 
		int affected = sqlSession.getMapper(IBoardDAO.class).write(boardDTO);
		
		//결과 데이터를 JSON객체형태로 출력하기 위해 Map컬렉션 생성		
		Map<String, String> map = new HashMap<String, String>();		
		if(affected==1)
			map.put("result", "success");
		else
			map.put("result", "fail");
		
		return map;		 
	} 
	
	/*
	React연동위해 추가한 메서드. 앞에서 작성한 메서드와 요청명이 동일하지만
	전송방식이 다르므로 각 요청에 따라 구분된다. 
	 */
	@RequestMapping(value="/restapi/boardWrite.do", method=RequestMethod.POST)
	@ResponseBody
	/*
	React에서 fetch() 함수를 통해 POST방식으로 요청한 데이터를 받을때
	body에 폼값을 실어서 보내므로 @RequestBody 어노테이션을 통해 받는다. 
	 */
	public Map<String, String> writePost(@RequestBody String data, BoardDTO boardDTO) {
		
		//전송받은 데이터를 디코딩 처리 한다. 
		System.out.println("write호출됨");
		data = URLDecoder.decode(data);
		System.out.println("data="+data);
		
		//json-simple 의존설정 필요. 해당 객체를 통해 JSON데이터를 파싱한다. 
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			//앞에서 디코딩 처리한 데이터를 파싱한다. 
			jsonObj = (JSONObject) jsonParser.parse(data);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}		
		System.out.println("아이디="+ jsonObj.get("id"));
		System.out.println("제목="+ jsonObj.get("title"));
		System.out.println("내용="+ jsonObj.get("content"));
		
		//파싱한 데이터를 DTO객체 저장한다. 
		boardDTO.setId(jsonObj.get("id").toString());
		boardDTO.setTitle(jsonObj.get("title").toString());
		boardDTO.setContent(jsonObj.get("content").toString());		 
		
		int affected = sqlSession.getMapper(IBoardDAO.class).write(boardDTO);
		Map<String, String> map = new HashMap<String, String>();
		
		if(affected==1)
			map.put("result", "success");
		else
			map.put("result", "fail");			
		
		return map;		 
	} 
}


  





