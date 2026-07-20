package de.linzn.growatt.webApi;

import com.sun.net.httpserver.HttpExchange;
import de.linzn.growatt.GrowattPlugin;
import de.linzn.growattJavaApi.devices.min.MinDeviceRealTimeData;
import de.linzn.webapi.WebApiPlugin;
import de.linzn.webapi.core.ApiResponse;
import de.linzn.webapi.core.HttpRequestClientPayload;
import de.linzn.webapi.modules.RequestInterface;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class GrowattWebApi extends RequestInterface {

    private GrowattPlugin growattPlugin;
    public GrowattWebApi(GrowattPlugin growattPlugin) {
        this.growattPlugin = growattPlugin;
    }

    @Override
    public Object callHttpEvent(HttpExchange httpExchange, HttpRequestClientPayload httpRequestClientPayload) throws IOException {
        ApiResponse apiResponse = new ApiResponse();

        MinDeviceRealTimeData minDeviceRealTimeData = this.growattPlugin.getGrowattManager().getMinDeviceRealTimeData();

        if(minDeviceRealTimeData != null){
            JSONObject data = new JSONObject();
            data.put("time", minDeviceRealTimeData.getTime());
            data.put("batteryState", minDeviceRealTimeData.getBatteryState());
            data.put("batteryChargePower", minDeviceRealTimeData.getBdc1ChargePower());
            data.put("batteryDischargePower", minDeviceRealTimeData.getBdc1DischargePower());
            data.put("batterySoc", minDeviceRealTimeData.getBmsSoc());
            data.put("pvPower",  minDeviceRealTimeData.getPpv());
            data.put("localLoadPower", minDeviceRealTimeData.getPacToLocalLoad());
            data.put("acPower", minDeviceRealTimeData.getPac());
            data.put("gridExportPower", minDeviceRealTimeData.getPacToGridTotal());
            data.put("gridImportPower", minDeviceRealTimeData.getPowerOfGridTake());

            apiResponse.getJSONObject().put("data", data);
            apiResponse.getJSONObject().put("lastSucessPollDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.growattPlugin.getGrowattManager().getLastSucessPollDate()));
        } else {
            apiResponse.getJSONObject().put("data", JSONObject.NULL);
            apiResponse.getJSONObject().put("lastSucessPollDate", JSONObject.NULL);
        }

        return apiResponse.buildResponse();
    }
}