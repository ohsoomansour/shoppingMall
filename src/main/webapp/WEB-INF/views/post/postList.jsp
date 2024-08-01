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
 
<script type="text/javascript">


$(document).ready(function(){
	/**/
	$('#zstree').jstree({ 
		'core' : {
			'data' : [
				{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
				{ "id" : "ajson2", "parent" : "#", "text" : "Root node 2" },
				{ "id" : "ajson3", "parent" : "ajson2", "text" : "Child 1" },
				{ "id" : "ajson4", "parent" : "ajson2", "text" : "Child 2" }
			]
		}
   	});
})

</script>

	<body>
		<div id="zstree"></div>
		<div class="board">	
			<p> 게시판 </p>
			<ul> 
				<c:forEach var="post" items="${posts}"> 
					<li>${post.p_id}</li>	
				</c>					
			</ul>
		</div>	
		
	</body> 
		
