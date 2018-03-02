

var tableVersion = 0;
var isfisrtSetup = false;
var refreshRate = 2000; //mili seconds
var GAME_TABLE_URL = buildUrlWithContextPath("gametable");
var num_of_players = 0 ;
var game_data =undefined;




function triggerAjaxTableContent() {
    setTimeout(ajaxChatContent, refreshRate);
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

function firstSetup(numOfPlayer){



    if(isfisrtSetup== true) return;
    var  css_data =get_board_location();
    console.log(css_data);
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
        console.log("I am in  5 init game positon");
        $("#pos1").css(css_data.positions.top_right).children(".bet").css(css_data.bet_positions.top);
        $("#pos2").css(css_data.positions.bottom_right).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos3").css(css_data.positions.bottom_left).children(".bet").css(css_data.bet_positions.bottom);
        $("#pos4").css(css_data.positions.top_left).children(".bet").css(css_data.bet_positions.top);
        $("#pos5").css(css_data.positions.top_middle).children(".bet").css(css_data.bet_positions.top);
        $("#pos6").remove();
        console.log("end of  5 init game positon");

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



function ajaxTableContent() {

    console.log("Not in good place");
    $.ajax({
        url: "/gametable",
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
            console.log(data);
            update_games_values(data);
            console.log("after the update game vals");
        },
        error: function (error) {
            console.log(error);
            alert("Faild to Print data")
            /*
            triggerAjaxTableContent();
            */
        }
    });
    console.log("after ajax");
}

function update_games_values(data) {
    console.log("In upder game values");
        if( num_of_players == 0) {
            num_of_players = data.number_of_players;
            console.log("the number of the player is " + num_of_players);

        }
        console.log("the number of the player is " + num_of_players);
        for (var i = 1 ; i<=num_of_players; i++) {
            console.log("I am in the loop " + i);
            $("#pos" + i.toString() + " .player_details .player_name").text(data.userData[i - 1].name);
            $("#pos" + i.toString() + " .player_details .type").text(data.userData[i - 1].type);
            $("#pos" + i.toString() + " .player_details .wins_count").text(data.userData[i - 1].num_of_wins);
            $("#pos" + i.toString() + " .player_details .chips").text(data.userData[i - 1].money);
            $("#pos" + i.toString() + " .bet").text(data.userData[i - 1].bid);


            if (data.userData[i - 1].role == "dealer") {
                $("#role" + i.toString()).css("background-image", "url('images/dealer.png')");
                console.log("I am " + i + " and i am dealer!");
            }
            else if (data.userData[i - 1].role == "big") {
                $("#role" + i.toString()).css("background-image", "url('images/big.png')");
                console.log("I am " + i + " and i am the big");
            }
            else if (data.userData[i - 1].role == "small") {
                $("#role" + i.toString()).css("background-image", "url('images/small.png')");
                console.log("I am " + i + " and i am th small!");
            }
            else {
                $("#role1" + i.toString()).css("background-image", "");
            }
                $("#pos" + i.toString() + " .player_details .cardplace .holecard1").css("background-image", createUrlForImage(data.userData[i - 1].cards[0]));
                $("#pos" + i.toString() + " .player_details .cardplace .holecard2").css("background-image", createUrlForImage(data.userData[i - 1].cards[1]));
                console.log("The image of the card for player "+ i + " is " + createUrlForImage(data.userData[i - 1].cards[0])+ " and " + createUrlForImage(data.userData[i - 1].cards[2]));
        }


}

function createUrlForImage(image_name)
{
    return "url(images/" +image_name + ".png";
}

$(document).ready(function(){
    console.log("start after loading");
    ajaxTableContent();
    console.log("first setup");
    console.log(num_of_players);
    firstSetup(num_of_players);
    raise_and_bet_scroller();

});


function  raise_and_bet_scroller() {


    $("#bet_raise_range").hide();
    $("#bet, #raise").click(function () {
        $("#bet_raise_range").animate({width: 'toggle'}, 600)});
    
}
