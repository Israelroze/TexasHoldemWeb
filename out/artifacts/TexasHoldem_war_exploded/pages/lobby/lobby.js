var chatVersion = 0;
var refreshRate = 2000; //mili seconds
var timeout=1000;


$(LoadGameFile());
$(PollUserlist());

function PollUserlist()
{
    console.log("poll on userlist activated");

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxUserList, refreshRate);

    setTimeout(ajaxUserList, refreshRate);
}

function ajaxUserList()
{
    console.log("sending poll userlist request");
    $.ajax({
        url:"/userlist",
        success: function(users) {
            console.log(users);
            tabletest(users);
        }
    });
}

function tabletest(users){
    $("#userlist tr").remove();
    var header = '<tr><th>' + "Username" + '</th>'
        + '<th>' + "Type" + '</th></tr>';
    $('#userlist').append(header);
    $.each(users,function(username, type) {
        var row = '<tr><td>' + username + '</td>'
            + '<td>' + type + '</td></tr>';
        $('#userlist').append(row);
    });
}

function refreshUserList(users){
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(username, type) {
        console.log("Adding user #" + username + ": " + type);
        console.log($("#userlist"));
        console.log($("#userlist").find('tbody'));
        $('#userslist').find('tbody').append('<tr><td>username</td><td>type</td></tr>');
        //$(  '<tr>' +
        //    '<td>'+username+'</td>'+
        //   '<td>'+type+'</td>'+
        //   '</tr>'
        //).appendTo($("#userslist"));
    });

    console.log($("#userlist").find('tbody').innerText);

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

