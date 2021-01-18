/**
 *  LAMS Air Quality
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
public static String version() { return "v0.1" }

import groovy.json.*
import java.text.SimpleDateFormat
import groovy.json.JsonSlurper

metadata {
    definition(name: "LAMS Air Quality", namespace: "LAMS", author: "LAMS", "vid": "500b24a6-8daa-349e-8dd2-836ce69c0bda", "mnmn": "SmartThingsCommunity", ocfDeviceType: "x.com.st.d.airqualitysensor") {
        capability "Air Quality Sensor" 
        capability "Carbon Dioxide Measurement" 
        capability "Fine Dust Sensor"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Tvoc Measurement"
        capability "Sensor"
        
        capability "Health Check"
        capability "Dust Sensor"
        capability "Carbon Monoxide Measurement"
        capability "connectprogram52293.ozoneMeasure"
        capability "connectprogram52293.nitrogenDioxideMeasure"
        capability "connectprogram52293.sufurDioxideMeasure"
        
        capability "connectprogram52293.station"
        capability "connectprogram52293.upate"



        command "refresh"
        command "setTemp"
        command "setHumid"
        command "setCo2"
        command "setCo"
        command "setVoc"
        command "setPm25"
        command "setPm10"

		command "setUpdate"
        command "setStation"
        command "setAirScore"
        command "setOzone"
        command "setSulfurDioxide"
        command "setNitrogenDioxide"
    }

    preferences {
        input type: "paragraph", element: "paragraph", title: "Version", description: version(), displayDuringSetup: false
        input "co2homekit", "number", title:"CO2 Notice for Homekit", defaultValue: 1500, description:"Enter CO2 minimum value(Default 1500)", range: "*..*"
    }

    simulator {
        // TODO: define status and reply messages here
    }

    tiles {
    }
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

def installed() {
    log.debug "installed()"
    sendEvent(name: "labelA", value: "Value A", displayed: false)
    sendEvent(name: "labelB", value: "Value B", displayed: false)
}

def uninstalled() {
    log.debug "uninstalled()"
    unschedule()
}

def updated() {
    log.debug "updated()"
}

def refresh() {
    log.debug "refresh()"
}

def setTemp(val) {
	log.debug "set Temperature to: $val"
	sendEvent(name:"temperature", value: val, displayed: false)
}

def setHumid(val) {
	log.debug "set Humidity to: $val"
	sendEvent(name:"humidity", value: val, displayed: false)
}

def setCo2(val) {
	log.debug "set Carbon dioxide to: $val"
	sendEvent(name:"carbonDioxide", value: val, displayed: false)
}

def setCo(val) {
	log.debug "set Carbon monoxide to: $val"
	sendEvent(name:"carbonMonoxideLevel", value: val, displayed: false)
}

def setVoc(val) {
	log.debug "set Tvoc to: $val"
	sendEvent(name:"tvocLevel", value: val, displayed: false)
}

def setPm25(val) {
	log.debug "set Fine dust to: $val"
	sendEvent(name:"fineDustLevel", value: val, displayed: false)
}

def setPm10(val) {
	log.debug "set Dust to: $val"
	sendEvent(name:"fineDustLevel", value: val, displayed: false)
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
	sendEvent(name:"airQuality", value: val, displayed: false)
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

def pullData() {
}
