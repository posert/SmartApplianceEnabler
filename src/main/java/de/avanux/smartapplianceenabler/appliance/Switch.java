/*
 * Copyright (C) 2015 Axel Müller <axel.mueller@avanux.de>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package de.avanux.smartapplianceenabler.appliance;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class Switch extends GpioControllable implements Control {
    @XmlTransient
    private Logger logger = LoggerFactory.getLogger(Switch.class);
    @XmlAttribute
    private boolean reverseStates;
    @XmlTransient
    GpioPinDigitalOutput outputPin;

    @Override
    public void start() {
        logger.info("Switch uses pin " + getPin() + (reverseStates ? " (reversed states)" : ""));
        outputPin = getGpioController().provisionDigitalOutputPin(getPin(), adjustState(PinState.LOW));        
    }
    
    public boolean on(boolean switchOn) {
        logger.info("Switching " + (switchOn ? "on" : "off") + " pin " + getPin());
        outputPin.setState(adjustState(switchOn ? PinState.HIGH : PinState.LOW));
        return true;
    }

    public boolean isOn() {
        return adjustState(outputPin.getState()) == PinState.HIGH;
    }
    
    private PinState adjustState(PinState pinState) {
        if(reverseStates) {
            if(pinState == PinState.HIGH) {
                return PinState.LOW;
            }
            return PinState.HIGH;
        }
        return pinState;
    }
}