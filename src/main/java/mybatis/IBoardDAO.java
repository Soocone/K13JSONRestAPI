package mybatis;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public interface IBoardDAO {
	
	//게시물 개수 카운트
	public int getTotalCount(ParameterDTO parameterDTO);
	
	//페이징 적용된 게시물 가져오기
	public ArrayList<BoardDTO> listPage(ParameterDTO parameterDTO);
	
	//게시물 내용보기
	public ArrayList<BoardDTO> view(ParameterDTO parameterDTO);
	
	
}
