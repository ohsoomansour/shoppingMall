<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
      var node = data.instance.get_node(data.selected[0]);
      var node_url = node.original.menu_url;
      window.location.href = node_url;
    }).on("loaded.jstree", function(event, data){
      $("#jstree").jstree('open_all')
    })
    });
</script>
      

  
    
