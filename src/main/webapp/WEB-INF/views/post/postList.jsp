<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script src="/js/lms/control/LocalStorageCtrl.js"></script> 
<title>게시판</title>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- jsTree -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/themes/default/style.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/jstree.min.js"></script>
<link rel="stylesheet" type="text/css" href="/css/post/post.css">

<script type="text/javascript">
/*
 var data = 
 
 */


$(document).ready(function() {
	/*sessionStorage에서 get 가져와서 -> data */
		var data = sessionStorage.getItem('loginMenu');
		console.log(data);
		if (data) {
	        data = JSON.parse(data);
	    } else {
	        data = []; // 기본값 설정 (데이터가 없을 경우 빈 배열)
	    }
        $('#jstree').jstree({
            'core' : {
                'data' : data
            }
        }).on("changed.jstree", function (e, data){
			console.log(data.selected) //목록 클릭 원소 
			/* data 받아오는 DB에서 URL 경로를 받아와서 클릭시 이동 
			if(data.selected[0] === "j1_1"){
				window.location.href = "/"
			} */
		}).on("loaded.jstree", function(event, data){
			$("#jstree").jstree('open_all')
		})
    });


</script>

<body>        
	<div id="jstree"></div>
	<div class="post">	
		<table id="p_table"> 
		  <tr>
  		  <th>no</th>
  			<th>제목</th>
  			<th>작성자</th>
        <th>사용자 구분</th>
  			<th>내용</th>
  			<th>작성시간</th>
        <th>조회수</th>
		  </tr>
			<c:forEach var="post" items="${posts}" > 
				<tr>
					<td>${post.p_id}</td>
					<td>${post.p_title}</td>
					<td>${post.u_name}</td>
           <c:choose>
            <c:when test="${post.u_type == 'G'}" >
               <td>고객</td>  
            </c:when>
            <c:when test="${post.u_type == 'A'}" >
              <td>관리자</td>  
            </c:when>
            <c:otherwise>
              <td>-</td>          
            </c:otherwise>
           </c:choose>
            
					<td>${post.p_contents}</td>
					<td>${post.created_at}</td>
					<td>${post.p_view}</td>
          
        </tr>	
			</c:forEach>					
		</table>
	</div>	
    
</body> 
		
