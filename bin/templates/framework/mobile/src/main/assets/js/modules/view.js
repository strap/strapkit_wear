var klass = require('klass');

var View = klass({
	config: null,
	initialize: function(config) {
		this.config = config;
	},
	getConfig: function() {
		return this.config;
	},
	getJSON: function() {}
})

module.exports = View;