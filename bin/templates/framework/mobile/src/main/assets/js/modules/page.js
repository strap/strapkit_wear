
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
		this.views.push(view);
		console.log("View count: " + this.views.length);
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
});
