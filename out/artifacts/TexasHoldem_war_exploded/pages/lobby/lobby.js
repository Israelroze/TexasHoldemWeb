var chatVersion = 0;
var refreshRate = 2000; //mili seconds


$(LoadGameFile());
$(PollUserlist());

function PollUserlist()
{
    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxUserList, refreshRate);

    setTimeout(ajaxChatContent, refreshRate);
}

function ajaxUserList()
{
    $.ajax({
        url:"/userlist",
        success: function(users) {
            refreshUserList(users);
        }
    });
}

function refreshUserList(users){
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(username, type) {
        console.log("Adding user #" + username + ": " + type);
        $(  '<tr>' +
            '<td>'+username+'</td>'+
            '<td>'+type+'</td>'+
            '</tr>'
        ).appendTo($("#userslist"));
    });
}

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

