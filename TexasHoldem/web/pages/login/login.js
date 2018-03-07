

var LOGIN_URL = buildUrlWithContextPath("login");



$(function(){
    $("#userform").submit(function(event) {
        event.preventDefault();
        $.ajax({
            //method:this.method,
            method: "POST",
            data: $("#userform").serialize(),
            url:LOGIN_URL ,
            timeout: 4000,
            success: function(r){
                console.log(r);
                console.log("redirecting to "+r);
                window.location.href=r;
            },
            error: function(r) {
                console.log("ERROR"+r.responseText);
                $("#errormessage").text(r.responseText).css({'color': 'red'});
            }
        });
    });
});



