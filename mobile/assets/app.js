var View = function(text, subView, onTouch) {
  this.text = text;
  this.subView = subView;
  this.onTouch = onTouch;

};

window.strapkit = {
    views: [],
    
    setView: function(viewText, viewID) {
        this.views[viewID] = view;
        window.strapkit_bridge.setView(view.text, viewID);
    }
}

var view = new View("Hello, JS World!",null, function(evt) {
  console.log(evt);
});
strapkit.setView(view, "1");