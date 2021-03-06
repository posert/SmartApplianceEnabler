/*
 * Copyright (C) 2017 Axel Müller <axel.mueller@avanux.de>
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
package de.avanux.smartapplianceenabler.control;

import de.avanux.smartapplianceenabler.modbus.ModbusSlave;
import de.avanux.smartapplianceenabler.modbus.ReadCoilExecutor;
import de.avanux.smartapplianceenabler.modbus.WriteCoilExecutor;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

public class ModbusSwitch extends ModbusSlave implements Control {

    private transient Logger logger = LoggerFactory.getLogger(ModbusSwitch.class);
    @XmlAttribute
    private String registerAddress;
    transient List<ControlStateChangedListener> controlStateChangedListeners = new ArrayList<>();

    @Override
    public boolean on(LocalDateTime now, boolean switchOn) {
        boolean result = false;
        try {
            logger.info("{}: Switching {}", getApplianceId(), (switchOn ? "on" : "off"));
            WriteCoilExecutor executor = new WriteCoilExecutor(registerAddress, switchOn);
            executor.setApplianceId(getApplianceId());
            executeTransaction(executor, true);
            result = executor.getResult();

            for(ControlStateChangedListener listener : controlStateChangedListeners) {
                listener.controlStateChanged(now, switchOn);
            }
        }
        catch (Exception e) {
            logger.error("{}: Error switching coil register {}", getApplianceId(), registerAddress, e);
        }
        return switchOn == result;
    }

    @Override
    public boolean isOn() {
        boolean coil = false;
        try {
            ReadCoilExecutor executor = new ReadCoilExecutor(registerAddress);
            executor.setApplianceId(getApplianceId());
            executeTransaction(executor, true);
            coil = executor.getCoil();
        }
        catch (Exception e) {
            logger.error("{}: Error switching coil register {}", getApplianceId(), registerAddress, e);
        }
        return coil;
    }

    @Override
    public void addControlStateChangedListener(ControlStateChangedListener listener) {
        this.controlStateChangedListeners.add(listener);
    }
}
