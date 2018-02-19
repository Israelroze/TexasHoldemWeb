$(function(){
    $("#userform").submit(function(event) {
        event.preventDefault();
        $.ajax({
            method:this.method,
            data: $("#userform").serialize(),
            url:this.action,
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



