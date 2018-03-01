var chatVersion = 0;
var refreshRate = 2000; //mili seconds
var timeout=1000;

$(LoadGameFile);
$(PollUserlist);
$(PollGamelist);

/////////USER LIST FUNCTIONS
function PollUserlist() {
    console.log("poll on userlist activated");

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxUserList, refreshRate);
    setTimeout(ajaxUserList, refreshRate);
}

function ajaxUserList() {
    console.log("sending poll userlist request");
    $.ajax({
        url:"/userlist",
        success: function(users) {
            console.log(users);
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
    console.log("poll on gamelist activated");

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxGameList, refreshRate);
    setTimeout(ajaxGameList, refreshRate);
}

function ajaxGameList() {
    console.log("sending poll userlist request");
    $.ajax({
        url:"/gamelist",
        success: function(games) {
            refreshGamelist(games);
        }
    });
}




function refreshGamelist(games){
    $("#gamelist li").remove();
    console.log(games);
    $("#gamelist").append(...games.map(x => $('<li class="Gamelist_li"></li>').append(createGameBox(x))));
}


function createGameBox(game)
{
    return $('<div></div>').append($('<table class="GamesTable"></table>')).append(
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
            $('<td></td>').append($('<button></button>').text("Join").bind('click',function(){ joinGame(game.game_name)}))
        )
    );

}


function joinGame(id) {
    event.preventDefault();

    $.ajax({
        method: "POST",
        url:"/joingame",
        data: {
                "GameId" : id
        },
        success: function(games) {

        },
        error: function(e){

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
function LoadGameFile(){
    $("#GameFileUpload").submit(function(event) {
        event.preventDefault();

        var formdata=new FormData();
        formdata.append("gameFile",this[0].files[0]);

        $.ajax({
            method:this.method,
            data:formdata,
            url:this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function(e) {
                console.error("Failed to submit");
                $("#errormessage").text(e);
            },
            success: function(r) {
                $("#errormessage").text("");
            }
        });
    });
}

