jQuery(document).ready(function () {
	jQuery('#vmap_lcv_fc').vectorMap({
		map: 'usa_en',
		backgroundColor: null,
		color: '#ffffff',
		enableZoom: false,
		showTooltip: true,
		selectedColor: null,
		hoverColor: null,
		onRegionClick: function(event, code, region){
			event.preventDefault();
		}
	});
});


function ForwardCheck_FC_LCV(){
	document.getElementById("fwdBtn").disabled = true;
	document.getElementById("vmap_lcv_fc").innerHTML = "";
	var timeInterval = $("#inTimeInterval").val();
	console.log(timeInterval);
	jQuery(document).ready(function () {
			jQuery('#vmap_lcv_fc').vectorMap({
				map: 'usa_en',
				backgroundColor: null,
				color: '#ffffff',
				enableZoom: false,
				showTooltip: true,
				selectedColor: null,
				hoverColor: null,
				onRegionClick: function(event, code, region){
					event.preventDefault();
				}
			});

		<!-- Data Input -->
		$.getJSON('http://13.59.239.61/forward-check-lcv', function(jd) {
                var objStateNew = jd;
				console.log(objStateNew);

				function paint(obj)
				{
					$("#vmap_lcv_fc").vectorMap("set", "colors", obj);
				}

				var curr = "";

				for(i = 0; i < objStateNew.length; i++) {
					(function(i){
						var hi = objStateNew[i].state;
						var b = objStateNew[i].color;
						//document.getElementById("show_object_fc").innerHTML += "<p>State: " + hi.toUpperCase() + " | Color: " + b + "<br />";
						var new_object_state = JSON.parse('{"' + hi + '":"' + b + '"}');
						console.log(new_object_state);
						var node = document.createElement("p");                 // Create a <li> node
						var textnode = document.createTextNode("State: " + hi.toUpperCase() + ", Color: " + b);         // Create a text node
						node.appendChild(textnode);
						document.getElementById("runtime-info-fc-lcv").innerHTML = "Number of Assignments : " + objStateNew.length + "<br />Number of Colors Used : 4";
						window.setTimeout(function(){
						  paint(new_object_state);
						  document.getElementById("show_object_fc_lcv").appendChild(node);
						}, i * timeInterval);
					}(i));
				}
		});
	});
}

function ForwardCheckReset_FC_LCV(){
	document.getElementById("fwdBtn").disabled = false;
	document.getElementById("vmap_lcv_fc").innerHTML = "";
	document.getElementById("show_object_fc_lcv").innerHTML = "";
	ForwardCheck_FC_LCV();
}
