package mybatis;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ParameterDTO {
	
	private String num; //게시물 일련번호
	private String searchField; //검색할 필드명
	private ArrayList<String> searchTxt; //검색어(2개 이상을 처리하기 위해 List컬렉션 사용)
	//출력할 게시물의 구간을 설정
	private int start; //게시물 구간의 시작
	private int end; //게시물 구간의 끝
}
