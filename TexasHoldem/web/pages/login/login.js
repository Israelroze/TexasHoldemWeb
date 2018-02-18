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
            error: function(jqXHR, textStatus, errorThrown){
                if(jqXHR.responseText !== ''){
                    alert(textStatus+": "+jqXHR.responseText);
                }else{
                    alert(textStatus+": "+errorThrown);
                }
            }
        })

    })
}

