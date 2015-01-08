
var parseFeed = function(data, quantity) {
  var items = [];
  for(var i = 0; i < quantity; i++) {
    // Always upper case the description string
    var title = data.list[i].weather[0].main;
    title = title.charAt(0).toUpperCase() + title.substring(1);
 
    // Get date/time substring
    var time = data.list[i].dt_txt;
    time = time.substring(time.indexOf('-') + 1, time.indexOf(':') + 3);
 
    // Add to menu items array
    items.push({
      title:title,
      subtitle:time
    });
  }
 
  // Finally return whole array
  return items;
};
 
// Show splash screen while waiting for data
var splashPage = StrapKit.UI.Page();
 
// Text element to inform user
var card = StrapKit.UI.Card({
  title: 'Hello World!',
  body:'My first StrapKit App'
});

var cardClick = function() {
  var newPage = StrapKit.UI.Page();

  var text = StrapKit.UI.TextView({
    position: 'center',
    text: 'My first transition'
  });

  newPage.addView(text);
  newPage.show();
};

card.setOnClick(cardClick);
 
// Add to splashPage and show
splashPage.addView(card);
splashPage.show();

// Make request to openweathermap.org
StrapKit.HttpClient(
  {
    url:'http://api.openweathermap.org/data/2.5/forecast?q=London',
    type:'json'
  },
  function(data) {
    // Create an array of Menu items
    var menuItems = parseFeed(data, 10);
    
    var resultsPage = StrapKit.UI.Page();

    var resultText = StrapKit.UI.TextView({
        position: 'left',
        text: JSON.stringify(menuItems);
    });

    resultsPage.addView(resultText);
    resultsPage.show();

    // Construct Menu to show to user
    /*var resultsMenu = StrapKit.UI.ListView({
        items: menuItems
    });
 
    // Add an action for SELECT
    resultsMenu.onItemClick(function(e) {
          // Get that forecast
          var forecast = data.list[e.itemIndex];
         
          // Assemble body string
          var content = data.list[e.itemIndex].weather[0].description;
         
          // Capitalize first letter
          content = content.charAt(0).toUpperCase() + content.substring(1);
         
          // Add temperature, pressure etc
          content += '\nTemperature: ' + Math.round(forecast.main.temp - 273.15) + '°C' 
          + '\nPressure: ' + Math.round(forecast.main.pressure) + ' mbar' +
            '\nWind: ' + Math.round(forecast.wind.speed) + ' mph, ' + 
            Math.round(forecast.wind.deg) + '°';
         
              // Create the Card for detailed view
              var detailCard = StrapKit.UI.Card({
                title:e.item.subtitle,
                body: content
              });
              detailCard.show();
      });
 
    // Show the Menu, hide the splash
    resultsPage.addView(resultsMenu);
    resultsPage.show();
    splashWindow.hide();*/

  },
  function(error) {
    console.log("Download failed: " + error);
  }
);