jQuery(document).ready(function () {
		jQuery('#vmap').vectorMap({
			map: 'usa_en',
			backgroundColor: null,
			color: '#ffffff',
			enableZoom: true,
			showTooltip: true,
			selectedColor: null,
			hoverColor: null,
			onRegionClick: function(event, code, region){
				event.preventDefault();
			}
		});
	
	<!-- Data Input -->	
	var objstate = [
  {
    "state": "ak",
    "color": "Red"
  },
  {
    "state": "wa",
    "color": "Green"
  },
  {
    "state": "id",
    "color": "Red"
  },
  {
    "state": "or",
    "color": "Blue"
  },
  {
    "state": "nv",
    "color": "Green"
  },
  {
    "state": "ut",
    "color": "Blue"
  },
  {
    "state": "wy",
    "color": "Green"
  },
  {
    "state": "mt",
    "color": "Blue"
  },
  {
    "state": "sd",
    "color": "Red"
  },
  {
    "state": "nd",
    "color": "Green"
  },
  {
    "state": "mn",
    "color": "Blue"
  },
  {
    "state": "ia",
    "color": "Green"
  },
  {
    "state": "wi",
    "color": "Red"
  },
  {
    "state": "il",
    "color": "Blue"
  },
  {
    "state": "ne",
    "color": "Blue"
  },
  {
    "state": "co",
    "color": "Red"
  },
  {
    "state": "az",
    "color": "Yellow"
  },
  {
    "state": "nm",
    "color": "Green"
  },
  {
    "state": "ca",
    "color": "Red"
  },
  {
    "state": "ks",
    "color": "Green"
  },
  {
    "state": "ok",
    "color": "Blue"
  },
  {
    "state": "tx",
    "color": "Red"
  },
  {
    "state": "mo",
    "color": "Red"
  },
  {
    "state": "ky",
    "color": "Green"
  },
  {
    "state": "in",
    "color": "Red"
  },
  {
    "state": "oh",
    "color": "Blue"
  },
  {
    "state": "wv",
    "color": "Red"
  },
  {
    "state": "mi",
    "color": "Green"
  },
  {
    "state": "pa",
    "color": "Green"
  },
  {
    "state": "va",
    "color": "Blue"
  },
  {
    "state": "md",
    "color": "Yellow"
  },
  {
    "state": "tn",
    "color": "Yellow"
  },
  {
    "state": "ar",
    "color": "Green"
  },
  {
    "state": "nc",
    "color": "Red"
  },
  {
    "state": "de",
    "color": "Red"
  },
  {
    "state": "nj",
    "color": "Blue"
  },
  {
    "state": "dc",
    "color": "Red"
  },
  {
    "state": "ny",
    "color": "Red"
  },
  {
    "state": "la",
    "color": "Blue"
  },
  {
    "state": "ms",
    "color": "Red"
  },
  {
    "state": "ga",
    "color": "Green"
  },
  {
    "state": "al",
    "color": "Blue"
  },
  {
    "state": "sc",
    "color": "Blue"
  },
  {
    "state": "fl",
    "color": "Red"
  },
  {
    "state": "vt",
    "color": "Green"
  },
  {
    "state": "ma",
    "color": "Blue"
  },
  {
    "state": "nh",
    "color": "Red"
  },
  {
    "state": "ct",
    "color": "Green"
  },
  {
    "state": "ri",
    "color": "Red"
  },
  {
    "state": "me",
    "color": "Green"
  },
  {
    "state": "hi",
    "color": "Green"
  }
];

	
	function paint(obj)
	{
		$("#vmap").vectorMap("set", "colors", obj);
	}
	var curr = "";
	
	for(i = 0; i < objStateNew.length; i++) {
		(function(i){
			var hi = objStateNew[i].state;
			var b = objStateNew[i].color;
			var new_object = JSON.parse('{"' + hi + '":"' + b + '"}');
			console.log(new_object);
			window.setTimeout(function(){
			  paint(new_object);
			}, i * 600);
		}(i));
}
});