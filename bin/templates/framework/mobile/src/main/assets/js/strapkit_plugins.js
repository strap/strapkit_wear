strapkit.define('strapkit/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "modules/libs/klass.js",
        "id": "com.straphq.plugin.ui.libs.klass",
        "clobbers": [
            "klass"
        ]
    },
    {
        "file": "modules/page.js",
        "id": "com.straphq.plugin.ui.page",
        "clobbers": [
            "Page"
        ]
    }
];
module.exports.metadata =
// TOP OF METADATA
{
    "com.straphq.plugin.ui": "0.1.0"
}
// BOTTOM OF METADATA
});
