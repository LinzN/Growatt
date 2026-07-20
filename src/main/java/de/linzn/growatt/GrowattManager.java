/*
 * Copyright (c) 2026 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.growatt;

import de.linzn.growattJavaApi.GrowattClientApi;
import de.linzn.growattJavaApi.devices.min.MinDeviceRealTimeData;
import de.linzn.growattJavaApi.exceptions.GrowattApiException;
import de.linzn.growattJavaApi.exceptions.GrowattDeviceNotFoundException;
import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.pluginModule.STEMPlugin;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GrowattManager {
    private final STEMPlugin plugin;
    private final GrowattClientApi growattClientApi;
    private final String deviceSerialNumber;

    private Date lastSucessPollDate = null;
    private MinDeviceRealTimeData minDeviceRealTimeData = null;

    public GrowattManager(STEMPlugin plugin, String token){
        this.plugin = plugin;
        this.growattClientApi = new GrowattClientApi(token);
        this.deviceSerialNumber = this.plugin.getDefaultConfig().getString("growatt.deviceSerialNumber");
        STEMApp.getInstance().getScheduler().runRepeatScheduler(this.plugin, this::poll, 2, 3, TimeUnit.MINUTES);
    }


    public GrowattClientApi getGrowattClientApi() {
        return growattClientApi;
    }

    public void poll() {
        try {
            this.minDeviceRealTimeData = this.growattClientApi.getMinDeviceRealTimeData(this.deviceSerialNumber);
            this.lastSucessPollDate = new Date();
            STEMApp.LOGGER.DEBUG("New growatt device data for " + this.deviceSerialNumber);
        } catch (GrowattApiException | GrowattDeviceNotFoundException e) {
            STEMApp.LOGGER.ERROR(e);
        }
    }

    public MinDeviceRealTimeData getMinDeviceRealTimeData() {
        return minDeviceRealTimeData;
    }

    public Date getLastSucessPollDate() {
        return lastSucessPollDate;
    }
}
