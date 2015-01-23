

var stringify = function(obj) {
	var cache = [];
	  var json = JSON.stringify(obj, function(key, value) {
	    if (typeof value === 'function') {
	      return value.toString();
	    } else if (typeof value === 'object' && value !== null) {
	    	if (cache.indexOf(value) !== -1) {
	            // Circular reference found, discard key
	            return;
	        } else {
	        	cache.push(value);
	        	return value;
	        }
		} else {
			return value;
		}
	  });
	  cache = null;
	  return json;
};
var AndroidPage = Page.extend({
	id: -1,
	setId: function(idx) {
		if (this.id == -1) {
			this.id = getPageIndex();
		}
	},
	getId: function() {
		return this.id;
	},
	show: function() {
		this.pageOpen = true;
		this.setId(getPageIndex);
		var viewString = [];
		for (var i = 0; i < this.getViews().length; i++) {
			var view = this.getViews()[i];
			viewString.push(view.getJSON());
		}
		var json = {
			views: viewString,
			pageOpen: this.pageOpen,
			backgroundColor: this.backgroundColor,
			id: this.getId()
		};	
		window.strapkit_bridge.showPage(stringify(json));
	},
	hide: function() {
		this.pageOpen = false;
		window.strapkit_bridge.hidePage(this.getId());
	}
});

var index = 1;

var getPageIndex = function() {
	return index++;
};

var httpClient = function(opts, success, error) {
	ajax(opts, success, error);
};

var convertJavaObject = function(data) {
	data = JSON.parse(window.strapkit_bridge.getJavaJSONObject(data));
	return data;
}


var StrapKit = {
	UI: {
	    ListView : function(config){ return new ListView(config); },
	    Card : function(config){ return new Card(config); },
	    TextView : function(config) { return new TextView(config); },
	    Page : function(config) { return new AndroidPage(config); }
	},
	HttpClient: httpClient,
	Metrics: {
		init: function(app_id) {
			window.strapkit_bridge.initMetrics(app_id);
		},
		logEvent: function(event_path, event_data) {
			window.strapkit_bridge.logEvent(event_path, JSON.stringify(event_data));
		}
	}
}
