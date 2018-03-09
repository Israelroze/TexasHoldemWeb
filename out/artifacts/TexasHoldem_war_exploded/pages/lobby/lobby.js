var chatVersion = 0;
var refreshRate = 2000; //mili seconds
var timeout=1000;

var JoinGame_URL=buildUrlWithContextPath("joingame");
var GaseList_URL=buildUrlWithContextPath("gamelist");
var UserList_URL=buildUrlWithContextPath("userlist");
var FileUpload_URL=buildUrlWithContextPath("fileupload");
var GameReady_URL=buildUrlWithContextPath("gameready");
var Logout_URL=buildUrlWithContextPath("logout");

$(LoadGameFile);
$(PollUserlist);
$(PollGamelist);
$(Logout);

/////////USER LIST FUNCTIONS
function Logout(){
    $("#logout").bind('click',function(){ajaxLogout()});
}


function PollUserlist() {
    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxUserList, refreshRate);
    setTimeout(ajaxUserList, refreshRate);
}

function ajaxLogout(){
    event.preventDefault();

    $.ajax({
        url:Logout_URL,
        success: function(r) {
            //$("#errormessage").text("");
            console.log("redirecting to "+r);
            window.location.href=r;
        },
        error: function(e){
            if(e.responseText.includes("/login/"))
            {
                console.log("redirecting to "+e.responseText);
                window.location.href=e.responseText;
            }
            console.log("fromAjaxLogout error:"+e.responseText);
            //$("#errormessage").text(e.responseText).css({'color': 'red'});
        }
    });
}

function ajaxUserList() {
    $.ajax({
        url:UserList_URL,
        success: function(users) {
            refreshUserlist(users);
        }
    });
}

function refreshUserlist(users){
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

////////GAME LIST Functions

function PollGamelist(){
    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxGameList, refreshRate);
    setTimeout(ajaxGameList, timeout);

    //setInterval(ajaxReadyGame, refreshRate);
    //setTimeout(ajaxReadyGame, timeout);

}

function ajaxGameList() {
    $.ajax({
        url:GaseList_URL,
        success: function(games) {
            console.log(games);
            refreshGamelist(games);
        }
    });
}

function refreshGamelist(games){
    $("#gamelist li").remove();
    $("#gamelist").append(...games.map(x => $('<li class="Gamelist_li"></li>').append(createGameBox(x))));
}

function createGameBox(game) {
    return $('<div class="GameBox"></div>').append($('<table class="GamesTable"></table>')).append(
        $('<tr></tr>').append(
            $('<td></td>').text("Title:" + game.game_name),
            $('<td></td>').text("Buy:" +   game.buy ),
            $('<td></td>').text("Hands:" + game.num_of_hands),
            $('<td></td>').text("Players:" + game.registered_players + '/' +game.total_players)
        ),
        $('<tr></tr>').append(
            $('<td></td>').text("Uploader:" + game.uploader),
            $('<td></td>').text("Big/Small:" + game.big + '/'+game.small),
            $('<td></td>').text("       "),
            //$('<td></td>').append($('<button class="joinbutton"></button>').text("Join").bind('click',function(){ joinGame(game.game_name)}))
            $('<td></td>').append(DealGameStarted(game))
        )
    );

}

function DealGameStarted(game)
{
    console.log("Dealing game started");
    if(game.is_game_started) {
        console.log("put P");
        return $('<p></p>').text("Game Currently Running").css({'color': 'green'});
    }
    else{
        return $('<button class="joinbutton"></button>').text("Join").bind('click',function(){ joinGame(game.game_name)});
    }
}

function joinGame(id) {
    event.preventDefault();

    $.ajax({
        method: "POST",
        url:JoinGame_URL,
        data: {
                "GameId" : id
        },
        success: function(r) {

            $("#errormessage").text("");
            console.log("redirecting to "+r);
            window.location.href=r;
        },
        error: function(e){
            $("#errormessage").text(e.responseText).css({'color': 'red'});
        }

    });
}

function LoadGameFile(){
    $("#GameFileUpload").submit(function(event) {
        event.preventDefault();

        var formdata=new FormData();
        formdata.append("gameFile",this[0].files[0]);

        $.ajax({
            method:this.method,
            data:formdata,
            url:FileUpload_URL,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function(e) {
                console.error("Failed to submit");
                $("#errormessage").text(e.responseText).css({'color': 'red'});
            },
            success: function(r) {
                $("#errormessage").text("");
            }
        });
    });
}

function ajaxReadyGame(){
    $.ajax({
        url:GameReady_URL,
        success: function(r) {
            console.log(r);
            if(!r.includes("false")){
                //redirect
                console.log("redirecting to "+r);
                window.location.href=r;
            }
        },
        error: function(e) {
            $("#errormessage").text(e.responseText).css({'color': 'red'});
        }
    });
}

/*
function buildGameBox(game) {
    var GameBox=
            '<li class="Gamelist_li">'+
             '<div  id="GameNode" class="GameBox">'+
             '<table id=game.game_name cellpadding="0" cellspacing="30" border="0" class=GamesTable">' +
                '<tr>' +
                    '<td class="GameCol">' + "Title:" + game.game_name +'</td>' +
                    '<td class="GameCol">' + "Buy:" +   game.buy + '</td>' +
                    '<td class="GameCol">' + "Hands:" + game.num_of_hands +'</td>' +
                    '<td class="GameCol">' + "Players:" + game.registered_players + '/' +game.total_players +'</td>' +
                '</tr>'+
                '<tr>' +
                    '<td class="GameCol">' + "Uploader:" + game.uploader +'</td>' +
                    '<td class="GameCol">' + "Big/Small:" + game.big + '/'+game.small +'</td>' +
                    '<td class="GameCol">' + "    "  +'</td>'+
                    '<td class="GameCol">' + '<input type="button" id='+game.game_name+"_joinToGame" +' value="Join" class="uploadbutton" />' + '</td>'+
                '</tr>'+
             '</table>'+
            '</div>'+
            '</li>';

    return GameBox;
}
*/

