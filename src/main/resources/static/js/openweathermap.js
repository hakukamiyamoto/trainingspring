
//現在の天気を取得する場所の名前
let targetCityName = "Tokyo";
let appId = "54335887e1f0d009e455b78dabb20db6";

const requestUrl = "https://api.openweathermap.org/data/2.5/weather?APPID=" + appId + "&lang=ja&units=metric&q=" + targetCityName + ",jp;";

//Ajax通信用のオブジェクトを作成
let xhr = new XMLHttpRequest();

//通信方式とURLを設定
xhr.open("GET", requestUrl);

//通信を実行する
xhr.send();

//通信ステータスが変わったら実行される関数
xhr.onreadystatechange = function() {
	//通信が完了
	if (xhr.readyState == 4) {
		ShowTodaysWeather(xhr.responseText);
	}
}

const weatherIcons = {
	'Clouds': 'http://openweathermap.org/img/w/04d.png',
	'Snow': 'http://openweathermap.org/img/w/13d.png',
	'Rain': 'http://openweathermap.org/img/w/09d.png',
	'Clear': 'http://openweathermap.org/img/w/01d.png',
	'Fog': 'http://openweathermap.org/img/w/50d.png',
	'Mist': 'http://openweathermap.org/img/w/50n.png',
	'Haze': 'http://openweathermap.org/img/w/50d.png',
};

const iconUrl = weatherIcons[json.weather[0].main] || 'http://openweathermap.org/img/w/01n.png';
$("#weatherMark").html(`<img src='${iconUrl}' >`);


function getDiscription(disc) {
	const weatherDescriptions = {
		'clear sky': '快晴',
		'few clouds': '晴れ',
		'scattered clouds': '曇り時々晴れ',
		'broken clouds': '曇り',
		'overcast clouds': '曇り',
		'light intensity shower rain': 'にわか雨',
		'shower rain': 'にわか雨',
		'heavy intensity shower rain': 'にわか雨',
		'light rain': '小雨',
		'moderate rain': '雨',
		'heavy intensity rain': '大雨',
		'very heavy rain': '激しい大雨',
		'rain': '雨',
		'thunderstorm': '雷雨',
		'snow': '雪',
		'mist': '靄',
		'tornado': '強風'
	};

	const translatedDescription = weatherDescriptions[disc];
	if (translatedDescription) {
		$('#weather-discription').html(translatedDescription);
		return translatedDescription;
	} else {
		return disc;
	}
}

function ShowTodaysWeather(response) {
	let obj = JSON.parse(response);

	let weather = obj.weather[0].description;
	let translatedWeather = getDiscription(weather);  // 翻訳関数を呼び出す
	let city = obj.name;
	let temp = obj.main.temp;

	const iconUrl = weatherIcons[obj.weather[0].main] || 'http://openweathermap.org/img/w/01n.png';

	// ここでアイコンを表示する処理を追加
	document.getElementById("weatherMark").innerHTML = `<img src='${iconUrl}' >`;

	// HTML要素に天気情報をセット
	document.getElementById("weatherInfo").innerHTML =
		"現在の" + city + "の天気は" + translatedWeather + "。<br>" +   // 翻訳された天気の説明を使用
		"気温は" + temp + "度です。";
}


