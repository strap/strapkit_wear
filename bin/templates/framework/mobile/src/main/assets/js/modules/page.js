var klass = require('klass');


var Page = klass({
	views: [],
	pageOpen: false,
	initialize: function(views) {
		if (typeof views === 'Array') {
			this.views = views;
		} else if (views !== null && typeof views === 'object') {
			this.views = [];
			this.views.push(views);
		} else {
			this.views = [];
		}
	},
	addView: function(view) {
		console.log(view);
		this.views.push(view);
	},
	getViews: function() {
		return this.views;
	},
	show: function() {
		this.pageOpen = true;
	},
	hide: function() {
		this.pageOpen = false;
	}
})

module.exports = Page;