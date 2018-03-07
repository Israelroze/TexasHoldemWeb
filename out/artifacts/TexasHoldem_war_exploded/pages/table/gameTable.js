var tableVersion = 0;
var GAME_TABLE_URL = buildUrlWithContextPath("gametable");
var num_of_players = 0 ;
var game_data =undefined;
var refreshRate = 3000; //mili seconds
var timeout=1000;

//id of poll interval action
var PollGameInterval;
var PollGameReady;

//flags
var if_no_fatal_errors=false;
var print_log = true;
var isfisrtSetup = false;
var is_Bet_Option_displayed = false;

//$(init_countdown);
$(UpperMenu);
$(PollIsGameReady);

/*
$(document).ready(function(){
    init_countdown();
});
*/

///////////////////////////////////////////////////////////////////
////////////// Trigger Hand and Game
///////////////////////////////////////////////////////////////////
function UpperMenu(){
    $("#top_menu").append($('<button class="bar_button" id ="Ready"></button>').text("Ready").bind('click',function(){ajaxPlayerReady();}));
    $("#top_menu").append($('<button class="bar_button" id ="Buy"></button>').text("Buy").bind('click',function(){ajaxBuy();}));
    $("#top_menu").append($('<button class="bar_button" id ="LeaveGame"></button>').text("Leave Game").bind('click',function(){ajaxLeaveGame();}));
}
function init_countdown() {
    $("#on_board_item").hide();
    $(".positions").hide();

    var fiveSeconds = new Date().getTime() + 5000;
    $('#messages').countdown(fiveSeconds, function(event){
        var $this = $(this);
        $this.html(event.strftime('<p id="clk_title">New Hand Will Start In: <span id="clk">%H:%M:%S</span></p>'));
        $("#clk").css({'color': 'red'});
        $("#clk_title").css({'color': 'red','font-family': 'Arial, Helvetica, sans-serif', 'font-size': '17px'});
    }).on('finish.countdown', function() {
        $("#clk").remove();
        $("#clk_title").remove();
        ajaxStartGame();
        ajaxStartHand();
        $("#on_board_item").show();
        $(".positions").show();
        if(!if_no_fatal_errors)PollGameTable();
    });
}

function newHandCountdonw(message){
    $("#on_board_item").hide();
    $(".positions").hide();

    var fiveSeconds = new Date().getTime() + 5000;
    $('#messages').countdown(fiveSeconds, function(event){
        var $this = $(this);
        $this.html(event.strftime('<p id="clk_title">'+message+'<span id="clk">%H:%M:%S</span></p>'));
        $("#clk").css({'color': 'red'});
        $("#clk_title").css({'color': 'red','font-family': 'Arial, Helvetica, sans-serif', 'font-size': '17px'});
    }).on('finish.countdown', function() {
        $("#clk").remove();
        $("#clk_title").remove();

        if(message.includes("Game Is Over"))
        {
            ajaxQuitGame();
        }
        else
        {
            ajaxStartHand();
            PollGameTable();
            $("#on_board_item").show();
            $(".positions").show();
        }
    });
}

///////////////////////////////////////////////////////////////////
////////////// Server POlL's
///////////////////////////////////////////////////////////////////

function PollGameTable() {
    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    PollGameInterval =  setInterval(ajaxTableContent, refreshRate);
    //setTimeout(ajaxTableContent, refreshRate);
}

function PollIsGameReady(){
    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    PollGameReady = setInterval(ajaxReadyGame, refreshRate);
    //setTimeout(ajaxTableContent, refreshRate);
}

///////////////////////////////////////////////////////////////////
////////////// Server Queries
///////////////////////////////////////////////////////////////////
function ajaxStartGame(){

    $.ajax({
        url:"/startgame",

        success: function(r) {
            logPrint("from ajaxStartGame success"+r);
        },
        error: function(e){
            logPrint("from ajaxStartGame"+e.responseText);
        }
    });
}

function ajaxStartHand() {
    $.ajax({
        url:"/starthand",

        success: function(r) {
            logPrint("from ajaxStartHand success"+r);
            $("#top_menu button").remove();
            PollGameTable();
            isfisrtSetup=false;
        },
        error: function(e){
            logPrint("from ajaxStartHand"+e.responseText);
            if(e.responseText.includes("Started")){
                $("#top_menu button").remove();
                PollGameTable();
                isfisrtSetup=false;
            }
            else
            {
                displayEndGame();
            }
            // if_no_fatal_errors=true;
            //if(e.responseText.includes("no sufficient money")) newHandCountdonw("Game Is Over, Return to Lobby In:");
        }
    });
}

function ajaxQuitGame() {
    clearInterval(PollGameInterval);

    $.ajax({
        url:"/quitgame",

        success: function(r) {
            console.log(r);
            console.log("redirecting to "+r);
            window.location.href=r;
        },
        error: function(e){
            logPrint("from ajaxQuitGame"+e.responseText);
            if(e.responseText.includes("/pages/")){
                console.log("redirecting to "+e.responseText);
                window.location.href=e.responseText;
            }
        }
    });
}

function ajaxBetOptionContent() {
    $.ajax({
        url: "/getallowded",
        dataType: 'json',
        async : false,
        success: function (data) {
            update_bet_option(data);
            logPrint("inside ajaxGetMoves Function: got info");
        },
        error: function (error) {
            logPrint("inside ajaxGetMoves Function: got error:"+error.responseText);
        }
    });
}

function ajaxTableContent() {

    $.ajax({
        url: "/gametable",
        dataType: 'json',
        async : false,
        success: function (data) {
            logPrint("OK ajaxTableContent, got JSON ");
            logPrint(data);
            if(!isfisrtSetup)
            {
                firstSetup(data.number_of_players);

                isfisrtSetup = true;
            }
            checkGameStatus(data);
            update_games_values(data);
            if (data.winners.length !== 0) {
                clearInterval(PollGameInterval);
                display_winners(data);
            }
        },
        error: function (error) {
            logPrint("Get game data ended with the following error "+ error.responseText);
            logPrint("ending game....");
            clearInterval(PollGameInterval);
            //newHandCountdonw("Game Is Over, Return to Lobby In:");
        }
    });
}

function ajaxReadyGame(){
    $.ajax({
        url:"/gameready",
        success: function(r) {
            console.log(" from ajaxReadyGame:"+r);
            if(r.includes("true")){
                clearInterval(PollGameReady);
                ajaxStartGame();
                ajaxStartHand();
                //init_countdown();
            }
        },
        error: function(e) {
            $("#errormessage").text(e.responseText).css({'color': 'red'});
        }
    });
}

function ajaxBuy(){}

function ajaxPlayerReady(){
    $.ajax({
        url:"/playerready",

        success: function(r) {
            logPrint("from ajaxPlayerReady success"+r);
            $("#Ready").append('<span class="ui-icon ui-icon-check"></span>').css('background', 'gray').prop('disabled', true);

        },
        error: function(e){
            logPrint("from ajaxPlayerReady"+e.responseText);
        }
    });
}

function ajaxLeaveGame(){}

function ajaxInitReadyPlayers(){
    $.ajax({
        url:"/initready",

        success: function(r) {
            logPrint("from ajaxInitReadyPlayers success"+r);

        },
        error: function(e){
            logPrint("from ajaxInitReadyPlayers"+e.responseText);
        }
    });
}

function ajaxGameStatus(){
    $.ajax({
        url:"/gamestatus",

        success: function(r) {
            logPrint("from ajaxGameStatus success"+r);
            if(r.includes("Over"))
            {
                displayEndGame();
            }
            else {
                //clearTable();
                ajaxInitReadyPlayers();
                PollIsGameReady();
                UpperMenu();
            }
        },
        error: function(e){
            logPrint("from ajaxGameStatus"+e.responseText);
        }
    });
}

///////////////////////////////////////////////////////////////////
////////////// Upper menu
///////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////
////////////// Buttom menu
///////////////////////////////////////////////////////////////////

function update_bet_option(data) {

    logPrint("##  In Update bet option");

    $("#bottom_menu").empty();
    var move;
    if (data.call === true) $("#bottom_menu").append($('<button class="bar_button" id ="call"></button>').text("Call").bind('click',function(){SetMove("CALL",0);}));
    if (data.check === true) $("#bottom_menu").append($('<button class="bar_button" id ="check"></button>').text("Check").bind('click',function(){SetMove("CHECK",0);}));
    if (data.fold === true) $("#bottom_menu").append($('<button class="bar_button" id ="fold"></button>').text("Fold").bind('click',function(){SetMove("FOLD",0);}));
    if (data.bet === true) {
        $("#bottom_menu").append($('<button class="bar_button" id ="bet"></button>').text("Bet").bind('click',toggle_slider));
        move = "BET";
    }
    if (data.raise === true) {
        $("#bottom_menu").append($('<button class="bar_button" id ="raise"></button>').text("Raise").bind('click',toggle_slider));
        move = "RAISE";
    }


    let min = data.minVal;
    let max = data.maxVal;
    let avg = Math.floor((max+min)/2);

    $("#bottom_menu").append($('<div id="bet_raise_range" class="slidecontainer vbox main-center "></div>')
        .hide().
        append($('<input id="myRange"  class="slider"  type="range">').attr('max', max.toString()).attr('min',min.toString()).val(avg.toString())));


    $("#bottom_menu").append($('<p id="demo_p" =></p>').text("Value:").hide());
    $("#bottom_menu").append($('<p id = "demo"></p>').hide());

    $("#bottom_menu").append($('<button id="accept" class = "bar_button"></button>').hide().text("Accept").bind('click',function(){ SetMove(move,document.getElementById("myRange").value.toString())}));

    slider_update_value();

    /*
   if ( data.bet === true || data.raise === true)
   {

       let min = data.minVal;
       let max = data.maxVal;
       let avg = Math.floor((max+min)/2);

       $("#bottom_menu").append($('<div id="bet_raise_range" class="slidecontainer vbox main-center "></div>').append(
           $('<input id="myRange" type="range">').attr('max', max.toString()).attr('min',min.toString()).val(avg.toString())));

       $("#bottom_menu").append($('<button></button>').text("Accept").bind('click',function(){ SetMove("BET",$("#myRange").value)}))
   }



   if (data.call === false) $("#call").remove();
   if (data.check === false) $("#check").remove();
   if (data.fold === false) $("#fold").remove();
   if (data.bet === false) $("#bet").remove();
   if (data.raise === false) $("#raise").remove();

   if ( data.bet === false && data.raise === false)  $("#bet_raise_range").remove();
   else {
       let min = data.minVal;
       let max = data.maxVal;
       let avg = Math.floor((max+min)/2);
       $("#myRange").attr("min", min.toString()).attr("max",max.toString()).val(avg.toString());
   }
   slider_update_value();
   $("#call").bind('click',function(){SetMove("CALL",0)});
   $("#check").bind('click',function(){SetMove("CHECK",0)});
   $("#fold").bind('click',function(){SetMove("FOLD",0)});
   $("#accept_raise").bind('click',function(){SetMove("RAISE",document.getElementById("myRange").value)});
   $("#accept_bet").bind('click',function(){SetMove("BET",document.getElementById("myRange").value)});

   */
}

function toggle_slider() {

    $("#bet_raise_range").animate({width: 'toggle'}, 600);
    $("#accept").animate({width: 'toggle'}, 600);
    $("#demo").animate({width: 'toggle'}, 600);
    $("#demo_p").animate({width: 'toggle'}, 600);

}

function slider_update_value() {
    var slider = document.getElementById("myRange");
    var output = document.getElementById("demo");
    output.innerHTML = slider.value;

    slider.oninput = function () {
        output.innerHTML = this.value;
    }
}

function SetMove(move,value){
    logPrint("inside SetMove Function");
    $.ajax({
        method:"POST",
        url:"/setmove",
        data: {
            "move"  : move,
            "value" : value
        },
        success: function(r){
            logPrint("inside SetMove Function: got response:"+r);

        },
        error: function(e){
            logPrint("inside SetMove Function: got error:"+e.responseText);
        }

    })
    is_Bet_Option_displayed = false;
    $("#bottom_menu").empty();
}

///////////////////////////////////////////////////////////////////
////////////// Update data
///////////////////////////////////////////////////////////////////

function clearTable(){
    $("#pos1").remove();
    $("#pos2").remove();
    $("#pos3").remove();
    $("#pos4").remove();
    $("#pos5").remove();
    $("#pos6").remove();
    $("#board").empty();
    $("#pot").text("");
}

function firstSetup(numOfPlayer){

    if(isfisrtSetup== true) return;
    var  css_data =get_board_location();

    if (numOfPlayer == 6)
    {
        $("#pos1").css(css_data.positions.top_right).children(".bet").css(css_data.bet_positions.top);
        $("#pos2").css(css_data.positions.bottom_right).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos3").css(css_data.positions.bottom_middle).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos4").css(css_data.positions.bottom_left).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos5").css(css_data.positions.top_left).children(".bet").css(css_data.bet_positions.top);
        $("#pos6").css(css_data.positions.top_middle).children(".bet").css(css_data.bet_positions.top);
    }

    if (numOfPlayer == 5)
    {

        $("#pos1").css(css_data.positions.top_right).children(".bet").css(css_data.bet_positions.top);
        $("#pos2").css(css_data.positions.bottom_right).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos3").css(css_data.positions.bottom_left).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos4").css(css_data.positions.top_left).children(".bet").css(css_data.bet_positions.top);
        $("#pos5").css(css_data.positions.top_middle).children(".bet").css(css_data.bet_positions.top);
        $("#pos6").remove();

    }

    if (numOfPlayer == 4)
    {

        $("#pos1").css(css_data.positions.top_right).children(".bet").css(css_data.bet_positions.top);
        $("#pos2").css(css_data.positions.bottom_right).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos3").css(css_data.positions.bottom_left).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos4").css(css_data.positions.top_left).children(".bet").css(css_data.bet_positions.top);
        $("#pos5").remove();
        $("#pos6").remove();
    }

    if (numOfPlayer == 3)
    {

        $("#pos1").css(css_data.positions.top_right).children(".bet").css(css_data.bet_positions.top);
        $("#pos2").css(css_data.positions.bottom_middle).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos3").css(css_data.positions.top_left).children(".bet").css(css_data.bet_positions.top);
        $("#pos4").remove();
        $("#pos5").remove();
        $("#pos6").remove();

    }

    isfisrtSetup= false;
}

function display_winners(data) {
    // Get the modal
    var modal = document.getElementById('myModal');

    modal.style.display = "block";
    // When the user clicks on <span> (x), close the modal

    $("#winner_list").empty();
    $("#winner_list").text("And The winner(s) is: ");
    $("#winner_list").append($("<ul></ul>").append(...data.winners.map(x => $('<li></li>').text(x))));
    $("#winner_list").append($('<button class="popup_button"></button>').text("ok").bind('click',function(){close_modal()}));

    function close_modal() {
        modal.style.display = "none";
        ajaxGameStatus();
    }
    //setTimeout(close_modal, 5000);
}

function displayEndGame(){
    // Get the modal
    var modal = document.getElementById('myModal');

    modal.style.display = "block";
    // When the user clicks on <span> (x), close the modal

    $("#winner_list").empty();
    $("#winner_list").text("The Game Is Ended, Thank You For Playing");
    $("#winner_list").append($('<button class="popup_button"></button>').text("ok").bind('click',function(){close_modal()}));

    function close_modal() {
        modal.style.display = "none";
        ajaxQuitGame();
    }
    //setTimeout(close_modal, 5000);
}

function update_games_values(data) {
    if( num_of_players === 0) {
        num_of_players = data.number_of_players;

    }

    for (let i = 1 ; i<=num_of_players; i++) {
        $("#pos" + i.toString() + " .player_details .player_name").text(data.userData[i - 1].name);
        $("#pos" + i.toString() + " .player_details .type").text(data.userData[i - 1].type);
        $("#pos" + i.toString() + " .player_details .wins_count").text(data.userData[i - 1].num_of_wins);
        $("#pos" + i.toString() + " .player_details .chips").text(data.userData[i - 1].money+ '$');
        // $("#pos" + i.toString() + " .bet").text(data.userData[i - 1].bid);


        if (data.userData[i - 1].role === "dealer") {
            $("#role" + i.toString()).css("background-image", "url('images/dealer.png')");

        }
        else if (data.userData[i - 1].role === "big") {
            $("#role" + i.toString()).css("background-image", "url('images/big.png')");
            //  logPrint("I am " + i + " and i am the big");
        }
        else if (data.userData[i - 1].role === "small") {
            $("#role" + i.toString()).css("background-image", "url('images/small.png')");

        }
        else {
            $("#role1" + i.toString()).css("background-image", "");
        }
        if(data.userData[i - 1].cards.length === 2)
        {
            $("#pos" + i.toString() + " .player_details .cardplace .holecard1").css("background-image", createUrlForImage(data.userData[i - 1].cards[0]));
            $("#pos" + i.toString() + " .player_details .cardplace .holecard2").css("background-image", createUrlForImage(data.userData[i - 1].cards[1]));
        }
        else{
            $("#pos" + i.toString() + " .player_details .cardplace .holecard1").css({"filter":"blur(4px)", "-webkit-filter": "blur(4px)"});
            $("#pos" + i.toString() + " .player_details .cardplace .holecard2").css({"filter":"blur(4px)", "-webkit-filter": "blur(4px)"});
        }
    }
    $("#board").empty();
    $("#board").append(...data.table_data.communityCards.map(x => $('<div class="card boardcard"></div>').css("background-image", createUrlForImage(x))));

    $("#pot").text("Pot: " + data.table_data.pot.toString() + "$");
}

function createUrlForImage(image_name) {
    return "url(images/" +image_name + ".png";
}

function get_board_location() {

    return {"positions":
        {
            top_right: {
                "left": "74%",
                "top": "-1.0%",
                "transform": "rotate(30deg)"
            },
            bottom_right: {
                "left":"74%",
                "top":"81%",
                "transform": "rotate(-30deg)"
            },

            bottom_middle: {
                "left":"35%",
                "top":"98%",
                "transform": "rotate(0deg)"
            },

            bottom_left: {
                "left":"-5%",
                "top":"81%",
                "transform": "rotate(30deg)"
            },


            top_left: {
                "left":"-5%",
                "top":"-1%",
                "transform": "rotate(-30deg)"
            },

            top_middle: {
                "left":"35%",
                "top":"-18%",
                "transform": "rotate(0deg)"
            }
        },
        "bet_positions":{
            "top" :{
                "left":"40%",
                "top":"135%"
            },
            "bottom": {
                "left":"40%",
                "top":"-45%"
            }
        }
    }
}

function checkGameStatus(data) {
    let is_your_turn=data.game_status.is_your_turn;
    let is_hand_over=data.game_status.is_hand_over;

    if (is_your_turn && !is_hand_over) {
        if (!is_Bet_Option_displayed) {
            is_Bet_Option_displayed = true;
            ajaxBetOptionContent();
        }
    }
    else {
        is_Bet_Option_displayed = false;
        $("#bottom_menu").empty();
    }

}

///////////////////////////////////////////////////////////////////
////////////// Other
///////////////////////////////////////////////////////////////////

function logPrint(message) {
    if (print_log )
        console.log(message);
}

function ShowSlider(move) {
    let min = data.minVal;
    let max = data.maxVal;
    let avg = Math.floor((max+min)/2);

    $("#bottom_menu").append($('<div id="bet_raise_range" class="slidecontainer vbox main-center "></div>').hide().append(
        $('<input id="myRange" type="range">').attr('max', max.toString()).attr('min',min.toString()).val(avg.toString()))
        .animate({width: 'toggle'}, 600));
    $("#bottom_menu").append($('<p id "demo_p"></p>').text("value").append($('<span id = "demo"></span>')).animate({width: 'toggle'}, 600));

    $("#bottom_menu").append($('<button></button>').hide().text("Accept").bind('click',function(){ SetMove(move,$("#myRange").value)}).animate({width: 'toggle'}, 600));

    slider_update_value();
}

function  open_raise_and_bet_scroller() {


    $("#bet_raise_range ").hide();
    $("#accept").hide();
    $("#demo_p").hide();

    $("#bet").click(function () {
        $("#bet_raise_range").animate({width: 'toggle'}, 600);
        $("#accept").animate({width: 'toggle'}, 600);
        $("#demo_p").animate({width: 'toggle'}, 600);

    });
    $("#raise").click(function () {
        $("#bet_raise_range").animate({width: 'toggle'}, 600);
        $("#accept").animate({width: 'toggle'}, 600);
        $("#demo_p").animate({width: 'toggle'}, 600);
    });

}
