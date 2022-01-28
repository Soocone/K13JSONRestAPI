package com.kosmo.jsonrestapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JSONController {
	
	/*
	 jackson 라이브러리를 통해 Map 컬렉션은 JSON객체로,
	 List 컬렉션은 JSON 배열로 출력해준다.
	 요청명 매핑시 @ResponseBody 어노테이션을 추가한다.
	 */
	@RequestMapping("/jsonUse/jsonView.do")
	@ResponseBody
	public Map<String, Object> responseBodyView(){
		//JSON 객체 생성을 위해 Map 컬렉션을 생성한다.
		Map<String, Object> map = new HashMap<String, Object>();
		
		//Map에 put()을 통해 데이터를 저장한다.
		map.put("String", "나는 문자열이다");
		map.put("Number", 1004);
		map.put("Message", "JSON은 jackson 의존설정이 필요해용");
		
		//JSON 배열을 생성하기 위해 List 컬렉션을 생성한다.
		ArrayList<String> list = new ArrayList<String>();
		list.add("Collection");
		list.add("되게 편해요");
		list.add("배열보다 좋아요");
		
		//JSON 객체가 배열을 포함한 형태로 출력됨
		map.put("Collection", list);
		
		//출력결과는 JSON 객체 형태이다.
		return map;
	}
}
