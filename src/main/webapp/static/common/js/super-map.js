const MAP_HOST = window.isLocal ? window.server : "https://iserver.supermap.io";
const MAP_URL = MAP_HOST + "/iserver/services/map-china400/rest/maps/China_4490";
const MAP_KEY = "Ie2kriXZZz9GPevFDBQzgLRf";

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
