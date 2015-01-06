var ListView = require('./modules/list.js');
var Page = require('./modules/page.js');
var View = require('./modules/view.js');
var Card = require('./modules/card.js');

var APP = {};

var userInterface = {
    ListView : function(config){ return new ListView(config)},
    Card : function(config){ return new Card(config); },
    Page : function(config){ return Page(config); },
    TextView : function(config) { return new TextView(config); }
}

module.exports = {
    'UI' : userInterface,
    'HttpClient' : function() { return require('./modules/httpClient.js')}
};