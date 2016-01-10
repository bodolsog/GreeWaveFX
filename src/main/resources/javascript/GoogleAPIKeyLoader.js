var head = document.getElementsByTagName('head')[0];
var key = properties.getGoogleAPIKey();

var script = document.createElement('script');
script.type = 'text/javascript';
script.src = 'http://maps.google.com/maps/api/js?key=' + key + '&sensor=false';
head.appendChild(script);