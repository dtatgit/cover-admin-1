const MAP_HOST = window.isLocal ? window.server : "https://iserver.supermap.io";
const MAP_URL = MAP_HOST + "/iserver/services/map-china400/rest/maps/China_4490";
const MAP_KEY = "Ie2kriXZZz9GPevFDBQzgLRf";

//const MAP_HOST = window.isLocal ? window.server : "http://172.25.117.10:8081";
//const MAP_URL = MAP_HOST + "/geoesb/proxy/0cb38c2afd5c42368348371375eb48d3/452a43316547454a9614d7c16b8c1d2d";
//const MAP_KEY = "452a43316547454a9614d7c16b8c1d2d";

const MAP_RES = [0.0012576413977673302, 7.266547736220494E-4, 3.633273868110247E-4, 1.8166369340551236E-4, 9.083184670275618E-5, 4.5415923351378204E-5, 2.2707961675688977E-5, 1.1353980837844615E-5, 5.676990418922308E-6, 2.8384952094612792E-6, 1.4192476047306396E-6];
const MAP_EXT = [[39.44138536979536, 116.20900742871117], [39.83156398892147, 116.72188973334208]];

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
