

var output = '';
for (var property in StrapKit) {
  output += property + ': ' + StrapKit[property]+'; ';
}
console.log(output);

var newPage = new StrapKit.Core.UI.Page();

newPage.show(function(args) {
    console.log(args['message']);
});

setTimeout(function() {
    newPage.hide(function(args) {
        console.log(args);
    });
}, 3000);