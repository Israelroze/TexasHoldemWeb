$(LoginUser());


function LoginUser(){
    $("#userform").submit(function() {

        var formdata=new FormData();

        formdata.append("userName",this[0].text);
        formdata.append("isComputer",this[1].radio);

        $.ajax({
            method:this.method,
            data:formdata,
            url:this.action,
            timeout: 4000,
            error: function(e) {
                console.error("Failed to submit");
                $("#result").text(e);
            },
            success: function(r) {
                $("#result").text(r);
            }
        })
    })

}

function newGameNode(r)
{

}