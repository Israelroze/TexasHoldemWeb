var tableVersion = 0;
var isfisrtSetup = false;
var refreshRate = 2000; //mili seconds
var GAME_TABLE_URL = buildUrlWithContextPath("gametable");


function triggerAjaxTableContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

function firstSetup(numOfPlayer){

    if(isfisrtSetup== true) return;

    if (numOfPlayer == 6)
    {



    }

}

function enterData(data){



    $("#pos1 .player_details .player_name").text(data.userData[0].name);




}

function ajaxTableContent() {
    $.ajax({
        url: "/gametable",
        data: "tableversion=" + tableVersion,
        dataType: 'json',
        success: function(data) {



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
            /*

            console.log("Server table version: " + data.version + ", Current table version: " + tableVersion);

            if (data.version !== chatVersion) {
               tableVersion = data.version;
               enterData(data.entries);
                triggerAjaxTableContent();

                */

            alert("Trying to Print Data")
                console.log(data);

                            },
        error: function(error) {
            console.log(error);
            alert("Faild to Print data")
            /*
            triggerAjaxTableContent();
            */
        }
    });
}




$(document).ready(function(){

alert("Start Script");
ajaxTableContent();
});