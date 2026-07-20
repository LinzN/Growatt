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



import de.linzn.growatt.webApi.WebApiHandler;
import de.linzn.stem.modules.pluginModule.STEMPlugin;


public class GrowattPlugin extends STEMPlugin {

    public static GrowattPlugin growattPlugin;
    private String token;
    private GrowattManager growattManager;
    private WebApiHandler webApiHandler;


    public GrowattPlugin() {
        growattPlugin = this;
    }

    @Override
    public void onEnable() {
        this.token = this.getDefaultConfig().getString("growatt.token", "6eb6f069523055a339d71e5b1f6c88cc"); // Example Token
        this.getDefaultConfig().getString("growatt.deviceSerialNumber", "MIN035C003"); // Example Serial
        this.getDefaultConfig().save();
        this.growattManager = new GrowattManager(this, this.token);
        this.webApiHandler = new WebApiHandler(this);
    }

    @Override
    public void onDisable() {
    }

    public GrowattManager getGrowattManager() {
        return growattManager;
    }

}
