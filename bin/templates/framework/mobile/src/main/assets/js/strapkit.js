var ListView = require('./modules/list.js');
var Page = require('./modules/page.js');
var View = require('./modules/view.js');
var Card = require('./modules/card.js');
var TextView = require('./modules/text.js');
var klass = require('klass');
var util = require('util');

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
	show: function() {
		console.log('show');
		console.log(this);
		this.pageOpen = true;
		viewString = [];
		for (var i = 0; i < this.getViews().length; i++) {
			var view = this.getViews()[i];
			viewString.push(view.getJSON());
		}
		var output = {
			views: viewString,
			pageOpen: this.pageOpen
		};
		window.strapkit_bridge.showPage(stringify(output));
	},
	hide: function() {
		console.log('hide');
		this.pageOpen = false;
		window.strapkit_bridge.hidePage(this);
	}
})



var userInterface = {
    ListView : function(config){ return ListView(config)},
    Card : function(config){ return new Card(config); },
    TextView : function(config) { return new TextView(config); },
    Page : function(config) { return new AndroidPage(config)}
}



module.exports = {
    'UI' : userInterface
};