package mybatis;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

//그냥 명시적으로 달아둠
@Service
public interface IAndroidDAO {
	public ArrayList<MemberVO> memberList();
	
	public MemberVO memberLogin(MemberVO memberVO);
}
