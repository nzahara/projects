function getStatic(){
	jQuery(document).ready(function () {
		jQuery('#vmap1_aus').vectorMap({
			map: 'australia_en',
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
}

function BackTrack_aus(){
	document.getElementById("bckBtn").disabled = true;
	document.getElementById("vmap1_aus").innerHTML = "";
	var timeInterval = $("#inTimeInterval").val();
	jQuery(document).ready(function () {
			jQuery('#vmap1_aus').vectorMap({
				map: 'australia_en',
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

		$.getJSON('http://13.59.239.61/backtracking/aus', function(jd) {
                var objstate = jd;
				console.log(objstate);

				function paint(obj)
				{
					$("#vmap1_aus").vectorMap("set", "colors", obj);
				}

				var curr = "";

				for(i = 0; i < objstate.length; i++) {
					(function(i){
						var hi = objstate[i].state;
						var b = objstate[i].color;
						var new_object = JSON.parse('{"' + hi + '":"' + b + '"}');
						console.log(new_object);
						var node = document.createElement("p");                 // Create a <li> node
						var textnode = document.createTextNode("State: " + hi.toUpperCase() + ", Color: " + b);         // Create a text node
						node.appendChild(textnode);
						document.getElementById("runtime-info-bt-aus").innerHTML = "Number of Assignments : " + objstate.length + "<br />Number of Colors Used : 4";
						window.setTimeout(function(){
							document.getElementById("show_object_bt_aus").appendChild(node);
							paint(new_object);
						}, i * timeInterval);
					}(i));
				}
		});
	});
}

function BackTrackReset_aus(){
	document.getElementById("bckBtn").disabled = false;
	document.getElementById("vmap1_aus").innerHTML = "";
	document.getElementById("show_object_bt_aus").innerHTML = "";
	BackTrack();
}
