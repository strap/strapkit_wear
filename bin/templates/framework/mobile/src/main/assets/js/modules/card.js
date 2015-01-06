var klass = require('klass');
var View = require('./view.js');

var Card = View.extend({
	onClick: null,
	title: null,
	body: null,
	initialize: function(config) {
		this.config = config;
		this.title = config.title;
		this.body = config.body;
		this.onClick = config.onClick;
	},
	setOnClick: function(event) {
		this.onClick = event;
	},
	getJSON: function(){
		clickString = null;
		if (this.onClick != null) {
			clickString = this.onClick.toString();
		}
		return {
			type: 'card',
			onClick: clickString,
			title: this.title,
			body: this.body
		};
	}
})

module.exports = Card;