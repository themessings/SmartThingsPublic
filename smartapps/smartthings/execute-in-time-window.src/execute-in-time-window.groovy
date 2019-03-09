definition(
        name: "execute-in-time-window",
        namespace: "smartthings",
        author: "SmartThings",
        description: "Example for executing a SmartApp only within a specified time window",
        category: "",
        iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
        iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
        iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


    preferences {
        // Use a switch as the "trigger" to check if the current
        // time is within the time specified
        section("Which switch?") {
            input "theswitch", "capability.switch"
        }
        section("When to execute") {
            input "startTime", "time", title: "beginning time"
            input "endTime", "time", title: "ending time"
        }
    }

    def installed() {
        log.debug "Installed with settings: ${settings}"
        initialize()
    }

    def updated() {
        log.debug "Updated with settings: ${settings}"
        unsubscribe()
        initialize()
    }

    def initialize() {
        // When the switch is turned on or off, call
        // the switchHandler method
        subscribe (theswitch, "switch", switchHandler)
    }

    // called when the switch is turned on or off
    def switchHandler(evt) {
        if (getTimeOk()) {
            log.debug "within specified time window"
        } else {
            log.debug "not within specified time window"
        }
    }

    // return true if the current time is within the specified
    // time window, false otherwise.
    def getTimeOk() {
        def result = true
        if (startTime && endTime) {
            def currTime = now()

            // get a Date object for the times specified (using the
            // time zone for the user's installed location)
            def start = timeToday(startTime, location?.timeZone).time
            def stop = timeToday(endTime, location?.timeZone).time

            // see if the current time is within the specified window
            // if the user entered a stop time that is earlier than the start
            // time, return true if the current time is after the start time
            result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
        }
        result
    }