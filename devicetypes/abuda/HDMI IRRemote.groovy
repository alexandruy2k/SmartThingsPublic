/*
* Author: Alex Buda
*
* Device Handler for ESP8266
*/


preferences {
	section("External Access"){
		input "external_on_uri", "text", title: "External On URI", required: false
		input "external_off_uri", "text", title: "External Off URI", required: false
	}
    
	section("Internal Access"){
		input "internal_ip", "text", title: "Internal IP", required: false
		input "internal_port", "text", title: "Internal Port (if not 80)", required: false
		input "internal_on_path", "text", title: "Internal On Path (/blah?q=this)", required: false
		input "internal_off_path", "text", title: "Internal Off Path (/blah?q=this)", required: false
        input "internal_method","enum", title: "Internal Method (Defaults to GET)", required: false, options: ["GET","POST","PUT"]
		input "input_on_key_1","text", title: "On Key 1", required: false
        input "input_on_value_1","text", title: "On Value 1", required: false
        input "input_on_key_2","text", title: "On Key 2", required: false
        input "input_on_value_2","text", title: "On Value 2", required: false
        input "input_on_key_3","text", title: "On Key 3", required: false
        input "input_on_value_3","text", title: "On Value 3", required: false
		input "input_off_key_1","text", title: "Off Key 1", required: false
        input "input_off_value_1","text", title: "Off Value 1", required: false
	}
}



metadata {
	definition (name: "HDMI IRRemote", namespace: "abuda", author: "Alex Buda") {
		capability "Actuator" 
 		capability "Switch" 
 		capability "Momentary" 
 		capability "Sensor"
        capability "Button"
        
		attribute "switch2", "string"
        attribute "switch3", "string"

		command "channel2"
		command "channel3"
		command "off2"
		command "off3"
	}


	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("channel1", "device.switch", width: 1, height: 1, canChangeIcon: true) {
			state "off", label: '1', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '1', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("channel2", "device.switch2", width: 1, height: 1, canChangeIcon: true) {
			state "off", label: '2', action: "channel2", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '2', action: "off2", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("channel3", "device.switch3", width: 1, height: 1, canChangeIcon: true) {
			state "off", label: '3', action: "channel3", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '3', action: "off3", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
		main "channel1"
		details (["channel1","channel2","channel3"])
	}
}

def parse(String description) {
	log.debug(description)
}

def internal_method = internal_method ? internal_method : "POST"

def on() {
	if (external_on_uri){
		// sendEvent(name: "switch", value: "on")
		// log.debug "Executing ON"

		def cmd = "${settings.external_on_uri}";

		log.debug "Sending request cmd[${cmd}]"

			httpGet(cmd) {resp ->
				if (resp.data) {
					log.info "${resp.data}"
				} 
			}
	}
	if (internal_on_path){
		def port
			if (internal_port){
				port = "${internal_port}"
			} else {
				port = 80
			}
        def myGroovyMap = ""
        if (input_on_key_1){
        	myGroovyMap += "${input_on_key_1}=${input_on_value_1}"
        }

        def json = new groovy.json.JsonBuilder(myGroovyMap)
        log.debug json

		def result = new physicalgraph.device.HubAction(
				method: "${internal_method}",
				path: "${internal_on_path}",
				headers: [
					HOST: "${internal_ip}:${port}",
                    "Content-Type": "application/x-www-form-urlencoded"
				],
                body: myGroovyMap
        )
        sendHubCommand(result)
        log.debug "result" + result
        log.debug "Switching to Channel 1" 

	}
    sendEvent(name: "switch", value: "on")
    sendEvent(name: "switch2", value: "off")
    sendEvent(name: "switch3", value: "off")
}

def off() {

    log.debug "Executing OFF" 
    sendEvent(name: "switch", value: "off")
}

def off2() {

    log.debug "Executing OFF2"
    sendEvent(name: "switch2", value: "off")
}

def off3() {

    log.debug "Executing OFF3" 
    sendEvent(name: "switch3", value: "off")
}

def channel2() {
if (internal_on_path){
		def port
			if (internal_port){
				port = "${internal_port}"
			} else {
				port = 80
			}
        def myGroovyMap = ""
        if (input_on_key_2){
        	myGroovyMap += "${input_on_key_2}=${input_on_value_2}"
        }

        def json = new groovy.json.JsonBuilder(myGroovyMap)
        log.debug json

		def result = new physicalgraph.device.HubAction(
				method: "${internal_method}",
				path: "${internal_on_path}",
				headers: [
					HOST: "${internal_ip}:${port}",
                    "Content-Type": "application/x-www-form-urlencoded"
				],
                body: myGroovyMap
        )
        sendHubCommand(result)
        log.debug "result" + result
        log.debug "Switching to Channel 2" 

	}
    sendEvent(name: "switch2", value: "on")
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switch3", value: "off")

}

def channel3() {
if (internal_on_path){
		def port
			if (internal_port){
				port = "${internal_port}"
			} else {
				port = 80
			}
        def myGroovyMap = ""
        if (input_on_key_3){
        	myGroovyMap += "${input_on_key_3}=${input_on_value_3}"
        }

        def json = new groovy.json.JsonBuilder(myGroovyMap)
        log.debug json

		def result = new physicalgraph.device.HubAction(
				method: "${internal_method}",
				path: "${internal_on_path}",
				headers: [
					HOST: "${internal_ip}:${port}",
                    "Content-Type": "application/x-www-form-urlencoded"
				],
                body: myGroovyMap
        )
        sendHubCommand(result)
        log.debug "result" + result
        log.debug "Switching to Channel 3" 

	}
    sendEvent(name: "switch3", value: "on")
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switch2", value: "off")
}

