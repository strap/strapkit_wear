ViewType = {
  //generic views
  kViewTypeText: 1,
  kViewTypeList: 2,
  kViewTypeImage: 3,


  //wear-specific
  kViewTypeButton: 4
};

var View = function(subView, id, onTouch) {
  this.id = id;
  this.subView = subView;
  this.onTouch = onTouch;


};

var textView = function(text, subView, id, onTouch) {
  var view  = new View(null, id, onTouch);
  view.type = ViewType.kViewTypeText;
  view.text = text;
  return view;
};

var listView = function(items, subView, id, onTouch) {
  var view  = new View(null, id, onTouch);
  view.items = items;
  view.type = ViewType.kViewTypeList;
  return view;
};

window.strapkit = {

  strapmetrics: {
    init: function(appID) {
      window.strapkit_bridge.strapMetricsInit(appID);

    },

    logEvent: function(evt, data) {
      window.strapkit_bridge.strapMetricsLogEvent(evt, JSON.stringify(data));
    }
  },

  views: [],

  setTextView: function(view) {
    this.views[view.id] = view;
    window.strapkit_bridge.setTextView(view.text, view.id);
  },

  setListView: function(view) {
    this.views[view.id] = view;
    var arrayTxt = JSON.stringify(view.items);
    window.strapkit_bridge.setListView(view.text, view.id, arrayTxt);
  },

  onTouch: function(viewID, eventData) {
    console.error('onTouch received by Strapkit');
    this.views[viewID].onTouch(eventData);
  }


};
var times = 0;
strapkit.strapmetrics.init("tSGpQgSgjYZisap2o");

var textvw = new textView("Hello, Strapkit!", null, 'strapkit_init', function() {
  times++;
  this.text = "You clicked it " +times + " times!";
  strapkit.setTextView(this);
  strapkit.strapmetrics.logEvent("/click", {times: times});
});

var view = new View("Hello, JS World!",null, "4", function(evt) {
  console.log(evt);
});

strapkit.setTextView(textvw);