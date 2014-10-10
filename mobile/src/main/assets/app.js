window.onload = function() {
  //your code here

  var SK = require('strapkit');
  var SM = SK.metrics;
  var UI = SK.ui;
  var times = 0;
  SM.init({
    app_id:"tSGpQgSgjYZisap2o"
  });

  var menu =  UI.menu({
      sections: [{
        items: [{
          title: 'Strapkit',
          subtitle: 'Can do Menus'
        }, {
          title: 'Second Item',
          subtitle: 'Subtitle Text'
        }]
      }]
    });
  menu.show();
}