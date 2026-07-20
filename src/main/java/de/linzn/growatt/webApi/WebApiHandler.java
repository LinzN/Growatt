package de.linzn.growatt.webApi;

import de.linzn.growatt.GrowattPlugin;
import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.webapi.WebApiPlugin;
import de.linzn.webapi.modules.WebModule;

public class WebApiHandler {

    private final GrowattPlugin stemPlugin;
    private final WebModule stemWebModule;

    public WebApiHandler(GrowattPlugin stemPlugin) {
        this.stemPlugin = stemPlugin;
        stemWebModule = new WebModule("growatt");
        stemWebModule.registerSubCallHandler(new GrowattWebApi(this.stemPlugin), "latest");
        WebApiPlugin.webApiPlugin.getWebServer().enableCallModule(stemWebModule);
    }
}
