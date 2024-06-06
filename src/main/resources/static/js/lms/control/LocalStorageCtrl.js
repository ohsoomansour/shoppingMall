


function isEmpty(str) {
	return (str == null || typeof (str) == "undefined" || str.replace(/ /gi, "") == "") ? true : false;
};

var LocalStorageCtrl = function() {
	// 로그인
	localStorage.SAVEID = isEmpty(localStorage.SAVEID) ? "" : localStorage.SAVEID;
	localStorage.GOURL = isEmpty(localStorage.GOURL) ? "" : localStorage.GOURL;
	// 사용내역
	localStorage.OHIST = isEmpty(localStorage.OHIST) ? "{}" : localStorage.OHIST;

	// 공통 SessionStorage 정보
	sessionStorage.PAGE = isEmpty(sessionStorage.PAGE) ? "1" : sessionStorage.PAGE;
	sessionStorage.TCNT = isEmpty(sessionStorage.TCNT) ? "0" : sessionStorage.TCNT;
	// 메뉴
	sessionStorage.OMENU = isEmpty(sessionStorage.OMENU) ? "{}" : sessionStorage.OMENU;
	// 사용자정보
	sessionStorage.OUSER = isEmpty(sessionStorage.OUSER) ? "{}" : sessionStorage.OUSER;
	sessionStorage.OLGTS = isEmpty(sessionStorage.OLGTS) ? "{}" : sessionStorage.OLGTS;
	sessionStorage.USERNO = isEmpty(sessionStorage.USERNO) ? "" : sessionStorage.USERNO;
};

LocalStorageCtrl.prototype = {

	/***************************************************************************
	 * Setter
	 **************************************************************************/
	setSession : function(property, val) { // 범용적으로 사용
		sessionStorage.removeItem(property);
		switch (typeof val) {
			case "object"		: sessionStorage.setItem(property, JSON.stringify(val)); break;
			case "undefined"	: sessionStorage.setItem(property, ""); break;
			default	: sessionStorage.setItem(property, val);
		}
	},

	setProperty : function(property, val) { // 범용적으로 사용
		localStorage.removeItem(property);
		switch (typeof val) {
			case "object"		: localStorage.setItem(property, JSON.stringify(val)); break;
			case "undefined"	: localStorage.setItem(property, ""); break;
			default : localStorage.setItem(property, val);
		}
	},

	/***************************************************************************
	 * Getter
	 **************************************************************************/
	getSession : function(property) { // 범용적으로 사용
		var str = sessionStorage.getItem(property);
		return (typeof(str) == "undefined" || isEmpty(str))? "" : str;
	},

	getProperty : function(property) { // 범용적으로 사용
		var str = localStorage.getItem(property);
		return (typeof(str) == "undefined" || isEmpty(str))? "" : str;
	},

	getSessJson : function(property) { // 범용적으로 사용
		var str = sessionStorage.getItem(property);
		return JSON.parse((typeof(str) == "undefined" || isEmpty(str))? "{}" : str);
	},

	getPropJson : function(property) { // 범용적으로 사용
		var str = localStorage.getItem(property);
		return JSON.parse((typeof(str) == "undefined" || isEmpty(str))? "{}" : str);
	},

	/***************************************************************************
	 * Remove
	 **************************************************************************/
	delSession : function(property) { // 범용적으로 사용
		sessionStorage.removeItem(property);
	},
	delProperty : function(property) { // 범용적으로 사용
		localStorage.removeItem(property);
	},

	/***************************************************************************
	 * 쿠키 함수
	 **************************************************************************/
	// 쿠키 저장
	setCookie : function(pkey, pval, pexp) {
		var strExpires;
		var dtmToday = new Date();
		dtmToday.setDate(dtmToday.getDate() + pexp);
		strExpires = " expires=" + dtmToday.toGMTString() + ";"
		document.cookie = pname + "=" + pval + "; domain=" + document.domain + "; path=/;" + strExpires;
	},

	// 쿠키 반환
	getCookie : function(pkey) {
		var arrCookie = document.cookie.split("; ");
		for (var i = 0; i < arrCookie.length; i++) {
			var arrItem = arrCookie[i].split("=");
			if (pkey == arrItem[0]) return unescape(arrItem[1]);
		}
		return null;
	},

	/***************************************************************************
	 * 기타 함수
	 **************************************************************************/
	goLink : function(url) {
		this.setSession("PAGE", "1");
		this.setSession("TCNT", "0");
		location.href = url;
	},

	getStamp : function() {
		return new Date().getTime();
	},

	getToday : function() {
		var dt = new Date();
		var yy = dt.getFullYear();
		var mm = dt.getMonth() + 1;
		var dd = dt.getDate();
		if (mm < 10) mm = "0" + mm;
		if (dd < 10) dd = "0" + dd;
		return '' + yy + '' + mm + '' + dd;
	},

	getDateTime : function() {
		var dt = new Date();
		var yy = dt.getFullYear();
		var mm = dt.getMonth() + 1;
		var dd = dt.getDate();
		var hh = dt.getHours();
		var mi = dt.getMinutes();
		if (mm < 10) mm = "0" + mm;
		if (dd < 10) dd = "0" + dd;
		if (hh < 10) hh = "0" + hh;
		if (mi < 10) mi = "0" + mi;
		return '' + yy + mm + dd + hh + mi;
	},

	formatNumber : function(n) {
		var reg = /(^[+-]?\d+)(\d{3})/;
		n += "";
		while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
		return n;
	},

};
