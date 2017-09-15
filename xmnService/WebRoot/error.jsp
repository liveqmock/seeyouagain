<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>错误页面</title>
	<meta name="renderer" content="webkit"> <meta name="fragment" content="!"> 
    <meta name="format-detection" content="telephone=no"> 
    <meta name="google" value="notranslate"> 
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
    <meta http-equiv="Cache-Control" content="no-transform"> 
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <style type="text/css">
        body{
            font-family: Microsoft Yahei;
        }

        body,p,img,div{margin:0;padding:0;}
        .img{margin:50px auto 20px;text-align: center;}
        .img img{width:40px;height: auto;}
        .paragraph{text-align: center;}
        .pp-text{text-align: center;height:30px;line-height: 30px;}
        .err-btn{width: 100%;text-align: center;}
        .err-btn a{display: block;width: 30%;text-align: center;height: 40px;line-height: 40px;margin:0 auto;text-decoration: none;color:#fff;background: #49b4ff;border-radius: 20px;margin-top:50px;}
        
    </style>
</head>
<body>
    <div class="img">
        <img src="img/icon_wh.png">
    </div>
    <p class="paragraph">${data.info}</p>
    <br><br>
    <!--3为设置的秒数-->
    <p class="pp-text"><span id="pp-sec">5</span>s秒后自动跳回到登录页面</p> 
    <div class="err-btn">
        <a href="javascript:;" onclick="gotoLogin()">返回到登陆页面</a>
    </div>
    <script>
        ;(function(){
            
            setTimeout(function(){
                setTime();
            },1000);
            function setTime(){
                var sec = document.getElementById('pp-sec').innerHTML;
                if(parseInt(sec) == 1){
                    //跳转的地址
                    //window.location.href="/goto/login.jsp";
                    window.location.href="/goto/login.jsp";
                }else{
                    var cuTime = parseInt(sec) - 1;
                    document.getElementById('pp-sec').innerHTML = cuTime;
                    setTimeout(function(){
                        setTime();
                    },1000); 
                }
            }
        })();
	
	function gotoLogin(){
	 window.location.href="/goto/login.jsp";
	}
    </script>
</body>

</html>