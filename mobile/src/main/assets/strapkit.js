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

  this.on = function(evtName, callback) {
    if(evtName === 'touch') {
        this.onTouch = callback;
    }
  }

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

window.require = function(lib) {
    if(lib === 'strapkit') {
        return window.strapkit;
    }
}

window.strapkit = {

  //wear specific code
  wear: {
    confirmActivity: function(message) {
      window.strapkit_bridge.confirmActivity(message);
    }
  },

  //sensors

  sensor : {
    accelerometer: {

    },

    heartrate : {

    }
  },

  ui: {
    menu: function(menuConfig) {
        var view = new View(null, "menu_id", null);
        view.items = menuConfig.sections[0].items;
        view.type = ViewType.kViewTypeList;
        view.show = function() {
            window.strapkit.setListView(this);
        }
        return view;
    }
  },

  //hooks into strapmetrics
  metrics: {
    init:function(obj) {
        //Only use app id for now
        window.strapkit_bridge.strapMetricsInit(obj.app_id);
    },

    log: function(evt, data) {
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


}