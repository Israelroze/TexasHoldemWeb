$(function(){
    $("#userform").submit(function() {
        $.ajax({
            method:this.method,
            data: $("#userform").serialize(),
            url:this.action,
            timeout: 4000,
            success:function(){
                console.log("login is ok");
            },
            error: function(jqXHR, textStatus, errorThrown) {
                window.history.back();
                alert('An error occurred... Look at the console (F12 or Ctrl+Shift+I, Console tab) for more information!');
                $('#errormessage').style.color("red").innerHTML = 'your tip has been submitted!';
                console.log('jqXHR:');
                console.log(jqXHR);
                console.log('textStatus:');
                console.log(textStatus);
                console.log('errorThrown:');
                console.log(errorThrown);
            }
        });
    });
});



