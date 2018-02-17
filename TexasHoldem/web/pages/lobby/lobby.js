$(LoadGameFile());


function LoadGameFile(){
    $("#GameFileUpload").submit(function() {

        var formdata=new FormData();

        formdata.append("gamefile",this[0].files[0]);

        $.ajax({
            method:this.method,
            data:formdata,
            url:this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
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