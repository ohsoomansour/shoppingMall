<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script src="/js/lms/control/LocalStorageCtrl.js"></script> 
<title>게시판 작성</title>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- jsTree -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/themes/default/style.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/jstree.min.js"></script>
<link rel="stylesheet" type="text/css" href="/css/post/post.css">

<script type="text/javascript"> 
   $(document).ready(function(){
     $("#u_id").val(sessionStorage.getItem("u_id"));    
     $("#u_email").val(sessionStorage.getItem("u_email"));
    });
  
  function publishPost(e){
  e.preventDefault();
  var form = $('#writing_post_frm')[0]; // 폼 요소
  var data = new FormData(form);
  console.log(form);
  console.log(data); //FormData {}라고 출력되는 것은 정상, 브라우저 콘솔에서 FormData 객체의 내용을 직접 표시할 수 없기 때문입니다.
  $.ajax({
    url: "/post/pubPost.do",
    type: "POST",
    enctype : 'multipart/form-data',
    processData: false,
    contentType : false,
    cache : false,
    data: data,
    dataType: "json",
    sucess: function(res){
      alert('글 등록 성공!')
    },
    error: function(e){
      
    }
  });
    
  }

  
</script>

<body>
  <div id="jstree"></div>        
  <div class="writing_post">
    <h1>게시판 글 작성</h1>
    <form id="writing_post_frm" method="post" enctype="multipart/form-data" >
        <div>
            <label for="u_id" >회원 no</label>
            <input id="u_id" name="u_id"   />
        </div>
        <div>
          <label for="u_email" >아이디</label>
          <input type="text" id="u_email" name="u_email"/>
        </div>
        <div>
            <label for="p_desc" >설명</label>
            <input id="p_desc" name="p_desc"/>
        </div>
        <div>
            <label for="p_contents" >내용</label>
            <textarea id="p_contents" > </textarea>
        </div>
        <div> 
          <label for="file">사진 업로드</label>
          <input type="file" id="file" name="file" />
        </div> 
        <button onclick="publishPost(event);">작성 제출</button>
    </form>
  </div>
</body> 