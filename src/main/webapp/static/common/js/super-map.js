// const MAP_HOST = window.isLocal ? window.server : "https://iserver.supermap.io";
// const MAP_URL = MAP_HOST + "/iserver/services/map-china400/rest/maps/China_4490";
// const MAP_KEY = "Ie2kriXZZz9GPevFDBQzgLRf";

const MAP_HOST = window.isLocal ? window.server : "http://172.25.117.10:8081";
const MAP_URL = MAP_HOST + "/geoesb/proxy/0cb38c2afd5c42368348371375eb48d3/452a43316547454a9614d7c16b8c1d2d";
const MAP_KEY = "452a43316547454a9614d7c16b8c1d2d";

const MAP_RES = [0.0011883962612831169, 0.00068664550781250184, 0.00034332275390625092, 0.00017166137695312546, 8.583068847656273E-05, 4.2915344238281474E-05, 2.1457672119140622E-05, 1.0728836059570429E-05, 5.3644180297852147E-06, 2.6822090148927268E-06, 1.3411045074463634E-06]

const icons = {
	'current-location': L.icon({
		iconUrl: '${ctxStatic}/common/images/cover.png',
		iconSize: [32, 38]
	})
}

//检查地址
function checkUrl(url) {
	if (url === "") {
		console.log(resources.msg_fillInURL);
		return false;
	}
	return true;
}
