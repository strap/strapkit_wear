var ListView = function(config) {
	//Initialize listView
};

ListView.prototype.items = function(items) {
  // Set items.
  // item is of form: 
  // { 
  //	'title': 'userTitle',
  //	'subtitle': 'userSubtitle',
  //	'icon': 'path/to/icon'
  // }
  return this;
};

ListView.prototype.onClick = function(event) {
	// Click on a particular selection
	// Support going to a new page currently
};

module.exports = ListView;