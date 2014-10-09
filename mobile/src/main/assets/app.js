strapkit.init = function () {
  //your code here

  var times = 0;
  strapkit.strapmetrics.init("tSGpQgSgjYZisap2o");

  var textvw = new textView("Welcome to CookieClicker!!", null, 'strapkit_init', function() {
    times++;
    this.text = "You clicked it " +times + " times!";

    if(times == 10) {
        if(strapkit.wear) {
            strapkit.wear.confirmActivity("Congrats, you win!");
        }

    }
    strapkit.setTextView(this);
    strapkit.strapmetrics.logEvent("/click", {times: times});
  });
  strapkit.setTextView(textvw);
  /*\
  var items = [];
  $.getJSON(
          "http://www.reddit.com/r/pics.json?jsonp=?",
          function foo(data)
          {
            $.each(
              data.data.children.slice(0, 10),
              function (i, post) {
                items.push({text:post.data.title});
              }
            )
          }
        ).success(function(){strapkit.setListView(new listView(items, null, 'listview', function(){}));});*/

}