/**
 *  LAMS Air Quality Index
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
public static String version() { return "Air quality aqicn.com v0.5" }

import groovy.json.*
import java.text.SimpleDateFormat
import groovy.json.JsonSlurper

metadata {
    definition(name: "LAMS Air Quality Index", namespace: "LAMS", author: "LAMS", "vid": "ee4b5215-6dae-3583-b2b9-9f7e24b6035a", "mnmn": "SmartThingsCommunity", ocfDeviceType: "x.com.st.d.sensor.smoke") {
        capability "connectprogram52293.aqistate"
		capability "connectprogram52293.aqi" 
        capability "connectprogram52293.pm25aqi"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Health Check"
        capability "connectprogram52293.pm10aqi"
        capability "connectprogram52293.coaqi"
        capability "connectprogram52293.ozoneMeasure"
        capability "connectprogram52293.nitrogenDioxideMeasure"
        capability "connectprogram52293.sufurDioxideMeasure"
        capability "connectprogram52293.atmpressure"
        capability "connectprogram52293.windspeed"
        capability "connectprogram52293.station"
        capability "connectprogram52293.upate"



        command "refresh"
        command "setTemp"
        command "setHumid"
        command "setCo"
        command "setPm25"
        command "setPm10"
		command "setUpdate"
        command "setStation"
        command "setAirScore"
        command "setOzone"
        command "setSulfurDioxide"
        command "setNitrogenDioxide"
        command "setWindSpeed"
        command "setAirPressure"
        command "setAirState"
    }

    preferences {
        input type: "paragraph", element: "paragraph", title: "Version", description: version(), displayDuringSetup: false
        input name: "City", type: "text", title:"City or @StationId or geo:lat;long", description:"City to show air quality without spaces or @StationId or geo:lat;long", required: true
        input name: "Token", type: "text", title:"API Token", description:"Token for the aqicn.com API", required: true
        input name: "Units", type: "enum", title: "System of measurement", options: ["Metric", "Imperial"], description: "Choose your system of measurement", required: true
    }

    simulator {
    }

    tiles {
    }
}

def parse(String description) {
    log.debug "Parsing '${description}'"
}

def installed() {
    log.debug "installed()"
    init()
}

def uninstalled() {
    log.debug "uninstalled()"
    unschedule()
}

def init(){
    refresh()
    runEvery1Minute(refresh)
}

def updated() {
    log.debug "updated()"
    refresh()
}

def refresh() {
    log.debug "refresh()"
	updateData()
}

def setTemp(val) {
	log.debug "set Temperature to: $val"
	sendEvent(name:"temperature", value: val, displayed: false)
}

def setHumid(val) {
	log.debug "set Humidity to: $val"
	sendEvent(name:"humidity", value: val, displayed: false)
}

def setWindSpeed(val) {
	log.debug "set Wind speed to: $val"
	sendEvent(name:"windSpeed", value: val, displayed: false)
}

def setCo(val) {
	log.debug "set Carbon monoxide to: $val"
	sendEvent(name:"coAqiLevel", value: val, displayed: false)
}

def setAirPressure(val) {
	log.debug "set Air pressure to: $val"
	sendEvent(name:"atmPressureLevel", value: val, displayed: false)
}

def setPm25(val) {
	log.debug "set Fine dust to: $val"
	sendEvent(name:"pm25AqiLevel", value: val, displayed: false)
}

def setPm10(val) {
	log.debug "set Dust to: $val"
	sendEvent(name:"pm10AqiLevel", value: val, displayed: false)
}

def setStation(val) {
	log.debug "set Station to: $val"
	sendEvent(name:"station", "value": val, displayed: false)
}

def setUpdate(val) {
	log.debug "set Update to: $val"
	sendEvent(name:"update", "value": val, displayed: false)
}

def setAirScore(val) {
	log.debug "set display score to: $val"
	sendEvent(name:"aqiLevel", value: val, displayed: false)
}

def setAirState(val) {
	log.debug "set display score to: $val"
	sendEvent(name:"aqiState", value: val, displayed: false)
}

def setOzone(val) {
	log.debug "set Ozone to: $val"
	sendEvent(name:"ozoneLevel", value: val, displayed: false)
}

def setSulfurDioxide(val) {
	log.debug "set Sulfur Dioxide to: $val"
	sendEvent(name:"sulfurDioxideLevel", value: val, displayed: false)
}

def setNitrogenDioxide(val) {
	log.debug "set Nitrogen Dioxide to: $val"
	sendEvent(name:"nitrogenDioxideLevel", value: val, displayed: false)
}

def updateData(){
	def HOST = "http://api.waqi.info/feed/"
    def city = "${City}"
	def feed = city.toLowerCase()
//	log.debug feed
    def token = "${Token}"
    def units = "${Units}"
    def options = HOST + feed + "/?token=" + token
//	log.debug options
	httpGet(options) {resp ->
//	log.debug resp.data
    def result = (resp.data)
//	log.debug result.status

    
  try {
   if (result.status == "ok") {
   sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
   
    if (result.data.aqi == 0) {
   		sendEvent(name:"aqiState", value: "n/a", displayed: false)
    }
    if ((result.data.aqi > 0) && (result.data.aqi < 51)) {
   		sendEvent(name:"aqiState", value: "Good", displayed: false)
    }
    if ((result.data.aqi > 50) && (result.data.aqi < 101)) {
   		sendEvent(name:"aqiState", value: "Moderate", displayed: false)
    }
    if ((result.data.aqi > 100) && (result.data.aqi < 151)) {
   		sendEvent(name:"aqiState", value: "Unhealthy for Sensitive Groups", displayed: false)
    }
    if ((result.data.aqi > 150) && (result.data.aqi < 201)) {
   		sendEvent(name:"aqiState", value: "Unhealthy", displayed: false)
    }
    if ((result.data.aqi > 200) && (result.data.aqi < 301)) {
   		sendEvent(name:"aqiState", value: "Very Unhealthy", displayed: false)
    }
    if (result.data.aqi > 300) {
   		sendEvent(name:"aqiState", value: "Hazardous", displayed: false)
    }

    if (result.data.containsKey("aqi")) {
    sendEvent(name: "aqiLevel", value: result.data.aqi, unit: "AQI", displayed: true)
  	} else {
    sendEvent(name: "aqiLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("t")) {   
        if (units == "Imperial") {
        	sendEvent(name: "temperature", value: result.data.iaqi.t.v * 1.8 + 32, unit: "°F", displayed: true)
		} else {        
        	sendEvent(name: "temperature", value: result.data.iaqi.t.v, unit: "°C", displayed: true)
        }
    } else {
    sendEvent(name: "temperature", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("h")) {
        sendEvent(name: "humidity", value: result.data.iaqi.h.v, displayed: true)
    } else {
    sendEvent(name: "humidity", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("pm25")) {
        sendEvent(name: "pmpAqiLevel", value: result.data.iaqi.pm25.v, unit: "AQI", displayed: true)
        } else {
    sendEvent(name: "pmpAqiLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("co")) {
        sendEvent(name: "coAqiLevel", value: result.data.iaqi.co.v, unit: "AQI", displayed: true)
        } else {
    sendEvent(name: "coAqiLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("pm10")) {
        sendEvent(name: "pmgAqiLevel", value: result.data.iaqi.pm10.v, unit: "AQI", displayed: true)
        } else {
    sendEvent(name: "pmgAqiLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.containsKey("city")) {
        sendEvent(name: "station", value: result.data.city.name, displayed: true)
        } else {
    sendEvent(name: "station", value: "n/a", displayed: true)
    }
    if (result.data.containsKey("time")) {
        sendEvent(name: "update", value: result.data.time.s, displayed: true)
        } else {
    sendEvent(name: "update", value: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("o3")) {
        sendEvent(name: "ozoneLevel", value: result.data.iaqi.o3.v, displayed: true)
        } else {
    sendEvent(name: "ozoneLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("so2")) {
        sendEvent(name: "sulfurDioxideLevel", value: result.data.iaqi.so2.v, displayed: true)
        } else {
    sendEvent(name: "sulfurDioxideLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("no2")) {
        sendEvent(name: "nitrogenDioxideLevel", value: result.data.iaqi.no2.v, displayed: true)
        } else {
    sendEvent(name: "nitrogenDioxideLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("p")) {
        sendEvent(name: "atmPressureLevel", value: result.data.iaqi.p.v, unit: "hPa", displayed: true)
        } else {
    sendEvent(name: "atmPressureLevel", value: "0", unit: "n/a", displayed: true)
    }
    if (result.data.iaqi.containsKey("w")) {
    	if (units == "Imperial") {
        	sendEvent(name: "windSpeed", value: Math.round((result.data.iaqi.w.v * 0.62137) * 10) / 10, unit: "mph", displayed: true)
		} else {        
            sendEvent(name: "windSpeed", value: result.data.iaqi.w.v, unit: "Km/h", displayed: true)
        }
        } else {
    sendEvent(name: "windSpeed", value: "0", unit: "n/a", displayed: true)
    }
   } else {
   sendEvent(name: "DeviceWatch-DeviceStatus", value: "offline")

   sendEvent(name:"aqiState", value: "error", displayed: false)
   sendEvent(name: "aqiLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "temperature", value: "0", unit: " ", displayed: true)
   sendEvent(name: "humidity", value: "0", unit: " ", displayed: true)
   sendEvent(name: "pmpAqiLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "coAqiLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "pmgAqiLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "station", value: "error", displayed: true)
   sendEvent(name: "update", value: "error", displayed: true)
   sendEvent(name: "ozoneLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "sulfurDioxideLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "nitrogenDioxideLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "atmPressureLevel", value: "0", unit: " ", displayed: true)
   sendEvent(name: "windSpeed", value: "0", unit: " ", displayed: true)
   }
	} catch (e) {
    log.error "Exception caught while parsing data: "+e;
    }
   }
}
