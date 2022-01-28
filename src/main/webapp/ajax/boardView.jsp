<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>title</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
$(function() {
	//해당 버튼을 눌렀을때 ajax메서드를 호출한다.
    $('#btnBoard').click(function() {
    	
        $.ajax({
            type : 'get', //전송방식
            url : '../restapi/boardView.do', //요청 URL
            data : {num: $('#num').val()}, //파라미터
            contentType : "text/html;charset:utf-8",
            dataType : "json", //콜백데이터 타입
            success : sucCallBack, //요청에 성공했을때 호출되는 콜백 함수
            error : errCallBack //실패했을때 콜백 함수
        });
    });
	/*
	trigger() 함수는 특정 이벤트를 자동으로 발생시켜준다.
	아래의 경우 페이지가 로드되면 해당 버튼을 클릭해준다.
	*/
    $('#btnBoard').trigger('click');
});
function sucCallBack(resData) {
    let tableData = "";
    $('#td1').html(resData.num);
    $('#td2').html(resData.id);
    $('#td3').html(resData.postdate);
    $('#td4').html(resData.visitcount);
    $('#td5').html(resData.title);
    $('#td6').html(resData.content);
}

//실패했을때의 콜백 함수
function errCallBack(errData) {
    console.log(errData.status+":"+errData.statusText);
}
</script>
</head>
<body>
<div class="container">
	<h2>게시판 API 할용하여 내용 출력하기</h2>
	<table class="table table-bordered">
		<tr>
			<th>번호</th><td id="td1"></td>
			<th>아이디</th><td id="td2"></td>
		</tr>
		<tr>
			<th>작성일</th><td id="td3"></td>
			<th>조회수</th><td id="td4"></td>
		</tr>
		<tr>
			<th>제목</th><td colspan="3" id="td5"></td>
		</tr>
		<tr>
			<th>내용</th><td colspan="3" id="td6"></td>
		</tr>
	</table>
	<div>
		<input type="text" id="num" value="1"/>
		<input type="button" value="내용불러오기" id="btnBoard"/>
	
	</div>
</div>
</body>
</html>