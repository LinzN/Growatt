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
import de.linzn.stem.modules.pluginModule.STEMPlugin;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Polls the Growatt inverter every five minutes. Data and poll time are stored together,
 * so readers never see data without a matching timestamp.
 */
public class GrowattManager {
    private final STEMPlugin plugin;
    private final GrowattClientApi growattClientApi;
    private final String deviceSerialNumber;
    private volatile Snapshot snapshot = null;

    public GrowattManager(STEMPlugin plugin, String token) {
        this.plugin = plugin;
        this.growattClientApi = new GrowattClientApi(token);
        this.deviceSerialNumber = this.plugin.getDefaultConfig().getString("growatt.deviceSerialNumber");
        this.plugin.getStemApi().scheduler().runRepeating(this::poll, 2, 5, TimeUnit.MINUTES);
    }

    public GrowattClientApi getGrowattClientApi() {
        return growattClientApi;
    }

    public void poll() {
        try {
            MinDeviceRealTimeData data = this.growattClientApi.getMinDeviceRealTimeData(this.deviceSerialNumber);
            if (data != null) {
                this.snapshot = new Snapshot(data, new Date());
                this.plugin.getPluginLogger().debug("New growatt device data for " + this.deviceSerialNumber);
            }
        } catch (GrowattApiException | GrowattDeviceNotFoundException e) {
            this.plugin.getPluginLogger().error("Growatt poll failed for " + this.deviceSerialNumber, e);
        }
    }

    public MinDeviceRealTimeData getMinDeviceRealTimeData() {
        Snapshot current = this.snapshot;
        return current == null ? null : current.data();
    }

    public Date getLastSucessPollDate() {
        Snapshot current = this.snapshot;
        return current == null ? null : current.date();
    }

    /**
     * @return latest data together with its poll time, or null if nothing was received yet
     */
    public Snapshot getSnapshot() {
        return this.snapshot;
    }

    public record Snapshot(MinDeviceRealTimeData data, Date date) {
    }
}
