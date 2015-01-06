var klass = require('klass');
var View = require('./view.js');

/*
  item: {
    title: 'Title',
    subtitle: 'subtitle',
    icon: 'path/to/icon'
  }
*/
var ListView = View.extend({
  items: [],
  onItemClick: "",
  initialize: function(config) {
    this.config = config;
    this.items = config.items;
    this.onItemClick = config.onItemClick;
  },
  setItems: function(items) {
    this.items = items;
    return this.items;
  },
  getItems: function() {
    return this.items;
  },
  setOnItemClick: function(e) {
    this.onItemClick = e;
  },
  getJSON: function() {
    itemClickString = null;
    if (onItemClick !== null) {
      itemClickString = onItemClick.toString();
    }
    return {
      items: this.items,
      onItemClick: itemClickString,
      type: 'listView'
    };
  }
})

module.exports = ListView;