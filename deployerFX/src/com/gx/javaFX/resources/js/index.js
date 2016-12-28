function test(content){
    //document.body.innerHTML="<h1>fuck you</h1>"
    $(document.body).append("<h1>"+content+"</h1>")
}
function setPage(){
    $(document.body).append("setPage")
}
$(function(){
    $(document.body).append("<h1>fuck you again!</h1>")
    $("#exit").on("click",function(){
        //$(document.body).append("<h1>fuck you again!</h1>")
        //$("#log").append(location.href)
        //app.sayHello();
    })
})
