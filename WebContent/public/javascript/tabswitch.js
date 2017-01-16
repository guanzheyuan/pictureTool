//$(function(){
//    var tabNum=$(".important").find("li");
//    var contentNum=$(".contents").children();
//    $(tabNum).each(function(index){
//        $(this).mouseover(function(){
//            $(contentNum).eq(index).css({"display":"block"});
//            $(contentNum).eq(index).siblings().css({"display":"none"});
//            $(this).find("strong").css({"display":"block"});
//            $(this).siblings().find("strong").css({"display":"none"});
//        })
//    })
//})

$(function(){
    var tabNum=$(".product_ul").find("li");
    var contentNum=$(".contents").children();
    var timer;

    $(tabNum).each(function(index){
        $(this).hover(function(){
            var that=$(this)
             timer=window.setTimeout(function(){
                $(contentNum).eq(index).css({"display":"block"});
                $(contentNum).eq(index).siblings().css({"display":"none"});
                 that.find("a").css({"background":"#1dd2af","color":"#fff"});
                 that.siblings().find("a").css({"background":"#fff","color":"black"});
            },100)
        },function(){
            window.clearTimeout(timer);
        })
    })
})