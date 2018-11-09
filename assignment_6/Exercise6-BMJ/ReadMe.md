Assignment 6 - Texturing and Lighting
Mauro Quarta 1/3
Julius Oeftiger 1/3
Brian Schweigler 1/3

 * The billboard was fun to implement. First we had to set the billboard's angles to the actual x and y angles, instead of the init value of 0.0. For the colors we used a color picker to get a nice orange, the opaqueness was adjusted with the distance and to get a nice fading effect we used a sort of 'overflow' function. For the opaque part, it was multiplied by a factor to increase the general size of the halo.
	An issue that we had was forgetting to set the angles and the big one was forgetting the blend function, without it the result is just... very weird.

 * Phong lighting model: The code itself was fairly easy to implement (as we already did it before), but it was hard to figure out what we should do exactly with the code and how to use it in combination with solar_viewr.cpp. at first we didn't normalize a vector, so we got an interesting scene (look at wrong_phong imgage).

 * Shading the Earth: it just took some time to adjust everything, it was never quite right (look at wrong_earth image), until we managed to fix everything. At some point we mixed the textures assigning the wrong number, so the night texture was on when the sun was shining.

Have a nice day/night!
