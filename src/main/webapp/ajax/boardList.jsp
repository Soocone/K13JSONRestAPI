<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
$(function() {
    $('#btnBoard').click(function() {
        $.ajax({
            type : 'get',
            url : '../restapi/boardList.do',
            data : {nowPage : $('#nowPage').val()},
            contentType : "text/html;charset:utf-8",
            dataType : "json",
            success : sucCallBack,
            error : errCallBack
        });
    });
    $('#btnBoard').trigger('click');
});

function sucCallBack(resData) {
    let tableData = "";
    $(resData).each(function(index,data){
        tableData += ""
        + "<tr>"
        + "    <td>"+data.num+"</td>"
        + "    <td>"+data.title+"</td>"
        + "    <td>"+data.id+"</td>"
        + "    <td>"+data.postdate+"</td>"
        + "    <td>"+data.visitcount+"</td>"
        + "</tr>"
    });
    $('#show_data').html(tableData);
}

function errCallBack(errData) {
    console.log(errData.status+":"+errData.statusText);
}
</script>
</head>
<body>
<div class="container">
   <h2>게시판 API 활용하여 목록 출력하기</h2>
   
    <table class="table table-bordered">
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>아이디</th>
            <th>작성일</th>
            <th>조회수</th>
        </tr>
        <tbody id="show_data"></tbody>
    </table>

    <div>
        <select id="nowPage">
            <option value="1">1page</option>
            <option value="2">2page</option>
            <option value="3">3page</option>
            <option value="4">4page</option>
            <option value="5">5page</option>
        </select>
        <input type="button" value="목록불러오기" id="btnBoard">
    </div>
</div>
</body>
</html>