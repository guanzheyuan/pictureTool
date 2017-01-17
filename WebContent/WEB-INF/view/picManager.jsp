<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes">
<meta http-equiv=”X-UA-Compatible” content=”IE=edge,chrome=1″/> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>IMAGE TOOL</title>
<link href="<%=path%>/public/style/style_index.css" tppabs="style_index.css" rel="stylesheet" type="text/css" />
<!--link href="default/js/DD_belatedPNG.js" rel="stylesheet" type="text/js" /-->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/public/javascript/tabswitch.js" tppabs="tabswitch.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/public/style/button.css">
<!--[if lte IE 6]> 
<script src="DD_belatedPNG.js" tppabs="js/DD_belatedPNG.js" type="text/javascript"></script>
<script type="text/javascript">
DD_belatedPNG.fix('.foot_r span img,.foot_r li img,.kf a img,.title');
/* 将 .png_bg 改成你应用了透明PNG的CSS选择器*/
</script>
<![endif]-->
</head>
<script type="text/javascript">
/**
 * 初始化
 */
$(function(){
	init();sysInit();
     var tabNum=$(".important").find("li");
     var contentNum=$(".contents").children();
     var timer;
	  $(tabNum).each(function(index){
	        $(this).hover(function(){
	            var that=$(this)
	             timer=window.setTimeout(function(){
	                $(contentNum).eq(index).css({"display":"block"});
	                $(contentNum).eq(index).siblings().css({"display":"none"});
	                 that.find("a").css({"background":"#FFF","color":"#fff"});
	                 that.find("strong").show();
	                 that.siblings().find("strong").hide();
	                 that.siblings().find("a").css({"background":"#fff","color":"black"});
	            },100)
	        },function(){
	            window.clearTimeout(timer);
	        })
	    });
	  showFileName('file1','showFileName');
	  showFile('file1','showImg','picTd');
	  
	  showFileName('file2_1','showFileName2_1');
	  showFile('file2_1','showImg2_1','picTd2_1');
	  
	  showFileName('file2_2','showFileName2_2');
	  showFile('file2_2','showImg2_2','picTd2_2');
	  
	  showFileName('file3','showFileName3');
	  showFile('file3','showImg3','picTd3');
});
/**
 * 展示文件名称
 */
function showFileName(Id,showName){
	$("#"+Id).on("change","input[type='file']",function(){
	    var filePath=$(this).val();
	    if(filePath.indexOf("jpg")!=-1||filePath.indexOf("jpeg")!=-1||filePath.indexOf("png")!=-1){
	        $(".fileerrorTip").html("").hide();
	        var arr=filePath.split('\\');
	        var fileName=arr[arr.length-1];
	        $("."+showName).html(fileName);
	    }else{
	        $("."+showName).html("");
	        $("."+showName).html("您未上传文件，或者您上传文件类型有误！");
	        return false 
	    }
	});
};
/**
 * 展示文件
 */
function showFile(Id,imageId,tdId){
	$("#"+Id).on("change","input[type='file']",function(evt){    
	    // 如果浏览器不支持FileReader，则不处理    
	    if (!window.FileReader) return;    
	    var files = evt.target.files;    
	    for (var i = 0, f; f = files[i]; i++) {    
	        if (!f.type.match('image.*')) {    
	            continue;    
	        }    
	        var reader = new FileReader();    
	        reader.onload = (function(theFile) {    
	            return function(e) {    
	                // img 元素    
	                document.getElementById(imageId).src = e.target.result;    
	            };    
	        })(f);    
	        reader.readAsDataURL(f);    
	    }   
		$("#"+tdId).show();
	}); 
}
function init(){
	var msg = "${msg}";
	if("" != msg){
		alert(msg);		
	}
}
function sysInit(){
	var system ={
			win:false,
			mac:false
	}
	//检测平台 
    var p = navigator.platform; 
	 system.win = p.indexOf("Win") == 0; 
     system.mac = p.indexOf("Mac") == 0; 
     $("#mac").val(system.mac);
     $("#win").val(system.win);
     
     $("#mac2").val(system.mac);
     $("#win2").val(system.win);
}
/**
 * 图片加密（文字）提交
 */
function onSubmit(){
	var file1 = $("#selfile").val();
	var len = file1.lastIndexOf(".");
	file1 = file1.substring(len,file1.length);
	if(".jpg"==file1||".jpeg"==file1){
		$("#encryptForm").submit();
	}else{
		alert("请选择jpg格式的图片");
	} 
	
}

/**
 * 图片水印提交
 */
function onSubmit1(){
	var logoFile = $("#logoFile").val();
	var picFile =$("#picFile").val();
	if(""==logoFile){
		alert("请选择水印图片!");
		return;
	}
	if(""==picFile){
		alert("请选择图片！");
		return;
	}
	var len = logoFile.lastIndexOf(".");
	logoFile = logoFile.substring(len,logoFile.length);
	var len = picFile.lastIndexOf(".");
	picFile = picFile.substring(len,picFile.length);
	if(".jpg"!=logoFile && ".jpeg"!=logoFile && ".png"!=logoFile){
		alert("请选择.jpg、.jpeg、.png的图片");
		return;
	} 
	if(".jpg"!=picFile && ".jpeg"!=picFile ){
			alert("请选择jpg或者.jpeg的图片");
			return;
	}
	$("#waterForm").submit();
}

function onSubmit3(){
	$("#verifyForm").submit();
}
</script>
<style>
	.file {
    position: relative;
    display: inline-block;
    background: #D0EEFF;
    border: 1px solid #99D3F5;
    border-radius: 4px;
    padding: 4px 12px;
    overflow: hidden;
    color: #1E88C7;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
}
.file input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
}
.file:hover {
    background: #AADFFD;
    border-color: #78C3F3;
    color: #004974;
    text-decoration: none;
}
</style>
<body>
<div class="w1200">
	 <div class="title">
		  <h1><span>功能</span>展示</h1>
	 </div>
	 <ul class="important cl">
		 <li class="rcsp_1">
			<a><span></span></a>
			<p>图片标记</p>
			<strong><i></i></strong>
		 </li>
		 <li class="rcsp_5">
			<a><span></span></a>
			<p>图片水印</p>
			<strong style="display:none"><i></i></strong>
		 </li>
		 <li class="rcsp_2">
			<a><span></span></a>
			<p>图片验证</p>
			<strong style="display:none"><i></i></strong>
		 </li>
	 </ul>
	 <!-- 图片加密 -->
	 <div class="contents" >
 		<div class="cl">
 			<div class="important_r" >
				    <form id="encryptForm" action="<%=path%>/doUpload.do" method="post" enctype="multipart/form-data">
				    			<table width="50%"  border="0" align="center"  style="border-collapse:collapse">
				    			<input name="mac" id="mac" type="hidden"'>
				    			<input name="win"  id="win"  type="hidden">
						    				<tr style="display:none;" id="picTd">
						    					<td colspan="2" align="center">
						    						<div class="showFileName"></div>
						    						<img id="showImg" src="" width="150px" height="100px">
						    					</td>
						    				</tr>
										<tr>
						    					<td align="center">	
							    						<a id="file1" href="#" class="file">选择文件
	  													<input type="file"  id="selfile" name="firstFile" class="inputstyle">
													</a>
						    					</td>
						    					<td align="center">
						    							 	<a   class="button button-rounded button-tiny" onclick="javascript:onSubmit();" >提交文件</a>
						    					</td>
						    			</tr>
				    			</table>
				    </form>
 			</div>
 		</div>
 		<div class="cl" style="display: none">
			<div class="important_r">
				 <form id="waterForm" action="<%=path%>/doUploadWatermark.do" method="post" enctype="multipart/form-data">
				 		<input name="mac" id="mac2" type="hidden"'>
				    		<input name="win"  id="win2"  type="hidden">
				 		<table width="50%" border="0" align="center"  style="border-collapse:collapse">
				 				<tr>
				 						<td align="center"  id="picTd2_1">
			 									<div class="showFileName2_1"></div>
						    						<img id="showImg2_1" src="" width="150px" height="100px">
			 							</td>
			 							<td align="center"    id="picTd2_2">
			 									<div class="showFileName2_2"></div>
						    						<img id="showImg2_2" src="" width="150px" height="100px">
			 							</td>
				 				</tr>
				 				<tr  height="10px"><td></td></tr>
			 					<tr>
			 							<td>
		 										<a id="file2_1" href="#" class="file">选择文件（水印）
  													<input type="file" id="logoFile"  name="logoFile" class="inputstyle">
												</a>
			 							</td>
			 							<td>
			 									<a id="file2_2" href="#" class="file">选择文件（图片）
	  													<input type="file" id="picFile" name="picFile" class="inputstyle">
													</a>
			 							</td>
			 					</tr>
			 					<tr height="20px">
			 							<td colspan="2">
			 								<a   class="button button-rounded button-tiny" onclick="javascript:onSubmit1();" >提交文件</a>
			 							</td>
			 					</tr>
				 		</table>
				 </form>
			</div>
  		</div>
  		 <div class="cl" style="display: none">
		    <div class="important_r">
		    		 <form id="verifyForm" action="<%=path%>/doVerifyPic.do" method="post" enctype="multipart/form-data">
				    			<table width="50%" border="0" align="center"  style="border-collapse:collapse">
						    				<tr style="display:none;" id="picTd3">
						    					<td colspan="2" align="center">
						    						<div class="showFileName3"></div>
						    						<img id="showImg3" src="" width="150px" height="100px">
						    					</td>
						    				</tr>
										<tr>
						    					<td align="center">	
							    						<a id="file3" href="#" class="file">选择文件
	  													<input type="file"  name="firstFile3" class="inputstyle">
													</a>
						    					</td>
						    					<td align="center">
						    						<a   class="button button-rounded button-tiny" onclick="javascript:onSubmit3();" >提交文件</a>
						    					</td>
						    			</tr>
				    			</table>
				    </form>
		    </div>
		  </div>
    </div>
     <!-- 图片验证-->
</div>
</body>
</html>