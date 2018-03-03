var tableVersion = 0;
var isfisrtSetup = false;
var GAME_TABLE_URL = buildUrlWithContextPath("gametable");
var num_of_players = 0 ;
var game_data =undefined;
var print_log = true;
var refreshRate = 3000; //mili seconds
var timeout=1000;

$(document).ready(function(){
    ajaxTableContent();
    firstSetup(num_of_players);
    PollGameTable();
    countdown_to_start_hand();
});

///////////////////////////////////////////////////////////////////
////////////// Trigger Hand and Game
///////////////////////////////////////////////////////////////////

function countdown_to_start_hand() {
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
    });
}

function ajaxStartGame(){

    $.ajax({
        url:"/startgame",

        success: function(r) {
            logPrint(r);
            //$("#errormessage").text("");
        },
        error: function(e){
            logPrint(e.responseText);
            //$("#errormessage").text(e.responseText).css({'color': 'red'});
        }

    });
}

function ajaxStartHand() {
    $.ajax({
        url:"/starthand",

        success: function(r) {
            //logPrint(r);
            //$("#errormessage").text("");
        },
        error: function(e){
            logPrint(e.responseText);
            //$("#errormessage").text(e.responseText).css({'color': 'red'});
        }

    });
}

///////////////////////////////////////////////////////////////////
////////////// POLL Game Data
///////////////////////////////////////////////////////////////////

function PollGameTable() {
    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxTableContent, refreshRate);
    setTimeout(ajaxTableContent, refreshRate);
}

function ajaxTableContent() {

    $.ajax({
        url: "/gametable",
        dataType: 'json',
        async : false,
        success: function (data) {
            logPrint("OK ajaxTableContent ");
            checkGameStatus(data);
            update_games_values(data);
        },
        error: function (error) {
            logPrint("Faild to Print data of game table");
            logPrint(error.responseText);
            /*
            triggerAjaxTableContent();
            */
        }
    });

}

//to open bet option for the right player and close for others
function checkGameStatus(data)
{
    let is_your_turn=data.game_status.is_your_turn;
    if(is_your_turn) ajaxBetOptionContent();
    else $("#bottom_menu").empty();
}

///////////////////////////////////////////////////////////////////
////////////// Bet option menu
///////////////////////////////////////////////////////////////////

function ajaxBetOptionContent() {
    $.ajax({
        url: "/getallowded",
        dataType: 'json',
        async : false,
        success: function (data) {
            /*
             data is of the next form:

                {
   "dataVerion":10,
   "userData":[
      {
         "name":"Avishay",
         "bid":100,
         "num_of_wins":2,
         "money":2003,
         "type":"Computer",
         "cards":[
            "6D",
            "5D"
         ]
      },
      {
         "name":"p123",
         "bid":100,
         "num_of_wins":2,
         "money":2003,
         "type":"Computer",
         "cards":[
            "6D",
            "5D"
         ]
      },
      {
         "name":"p345",
         "bid":100,
         "num_of_wins":2,
         "money":2023403,
         "type":"Computer",
         "cards":[
            "6D",
            "5D"
         ]
      },
      {
         "name":"jho",
         "bid":100,
         "num_of_wins":2,
         "money":223003,
         "type":"Human",
         "cards":[
            "6D",
            "5D"
         ]
      },
      {
         "name":"sdf",
         "bid":100,
         "num_of_wins":2,
         "money":2003,
         "type":"Computer",
         "cards":[
            "6D",
            "5D"
         ]
      },
      {
         "name":"sdf",
         "bid":1234200,
         "num_of_wins":23,
         "money":234003,
         "type":"Computer",
         "cards":[
            "6D",
            "5D"
         ]
      }
   ],
   "num_of_games":10,
   "current_game_number":4,
   "number_of_players":6
}
             */
            update_bet_option(data);

        },
        error: function (error) {
            // logPrint(error);
            // logPrint("Faild to Print data bet Option");

        }
    });
}

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
    $.ajax({
        method:"POST",
        url:"/setmove",
        data: {
            move : move,
            value: value
        },
        success: function(r){

        },
        error: function(e){

        }
    })
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

///////////////////////////////////////////////////////////////////
////////////// Board Data
///////////////////////////////////////////////////////////////////

function update_games_values(data) {
    if( num_of_players === 0) {
        num_of_players = data.number_of_players;

    }

    for (let i = 1 ; i<=num_of_players; i++) {
//        $("#pos" + i.toString() + " .player_details .player_name").text(data.userData[i - 1].name);
        $("#pos" + i.toString() + " .player_details .type").text(data.userData[i - 1].type);
        $("#pos" + i.toString() + " .player_details .wins_count").text(data.userData[i - 1].num_of_wins);
        $("#pos" + i.toString() + " .player_details .chips").text(data.userData[i - 1].money+ '$');
        // $("#pos" + i.toString() + " .bet").text(data.userData[i - 1].bid);


        if (data.userData[i - 1].role == "dealer") {
            $("#role" + i.toString()).css("background-image", "url('images/dealer.png')");

        }
        else if (data.userData[i - 1].role == "big") {
            $("#role" + i.toString()).css("background-image", "url('images/big.png')");
            //  logPrint("I am " + i + " and i am the big");
        }
        else if (data.userData[i - 1].role == "small") {
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
    $("#board").append(...data.table_data.communityCards.map(x => $('<div class="card boardcard"></div>').css("background-image", createUrlForImage(x))));

    $("#pot").text("Pot: " + data.table_data.pot.toString() + "$");
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

///////////////////////////////////////////////////////////////////
////////////// Other
///////////////////////////////////////////////////////////////////

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

function createUrlForImage(image_name) {
    return "url(images/" +image_name + ".png";
}

function logPrint(message) {
    if (print_log )
        console.log(message);
}
