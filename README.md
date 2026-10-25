![MirraNET Logo](https://raw.githubusercontent.com/LinzN/Stem/master/stem-core/src/main/resources/header-logo.png)

# growatt

> Solar and battery data of a Growatt MIN inverter in [STEM](https://github.com/LinzN/Stem).

growatt reads the real-time data of a Growatt MIN inverter (with battery) from the Growatt cloud every
five minutes and offers it through [web-api](https://github.com/LinzN/web-api), for example for a dashboard.

---

## Features

- Poll every 5 minutes (first poll 2 minutes after start) with
  [growatt-java-api](https://github.com/LinzN/growatt-java-api)
- PV, load, grid and battery power plus today's energy values
- Data and poll time are always delivered together; failed polls keep the last data

---

## Requirements

| Requirement | Version                                        |
|-------------|------------------------------------------------|
| STEM        | 1.2.0 or newer (`stem-api`)                    |
| Plugins     | [web-api](https://github.com/LinzN/web-api)    |
| Growatt     | API token of the Growatt cloud and the serial number of the inverter |

---

## Installation

1. Build the plugin with `mvn clean package` (or take the build from Jenkins).
2. Copy `target/growatt.jar` into the `plugins` folder of STEM and the libraries from
   `target/libraries` as `libraries.zip` (folder `libraries` inside) into `plugins/growatt/`.
3. Start STEM once to create `plugins/growatt/config.yml`.
4. Enter token and serial number, then restart STEM (plugins are only loaded at startup).

---

## Configuration (`config.yml`)

| Key                          | Default                    | Description                         |
|------------------------------|----------------------------|-------------------------------------|
| `growatt.token`              | example token of Growatt   | API token of your Growatt account   |
| `growatt.deviceSerialNumber` | `MIN035C003` (example)     | Serial number of the inverter       |

---

## Web API

`/growatt/latest` returns the latest data:

| Field                       | Description                         |
|-----------------------------|-------------------------------------|
| `time`                      | Time of the data on the inverter    |
| `pvPower`                   | Current PV power                    |
| `acPower`                   | Current AC output power             |
| `localLoadPower`            | Power used by the house             |
| `gridExportPower`           | Power fed into the grid             |
| `gridImportPower`           | Power taken from the grid           |
| `batteryState`              | Battery state                       |
| `batterySoc`                | Battery state of charge in %        |
| `batteryChargePower`        | Battery charge power                |
| `batteryDischargePower`     | Battery discharge power             |
| `pvEnergyToday`             | PV energy today                     |
| `localLoadEnergyToday`      | Energy used by the house today      |
| `gridExportEnergyToday`     | Energy fed into the grid today      |
| `gridImportEnergyToday`     | Energy taken from the grid today    |
| `batteryChargeEnergyToday`  | Energy charged into the battery today |

The fields are inside `data`; `lastSucessPollDate` contains the time of the last successful poll. Both are
`null` until the first poll succeeded.

---

## License

Copyright (c) MirraNET, Niklas Linz. Licensed under the
[GNU Lesser General Public License v3.0](https://www.gnu.org/licenses/lgpl-3.0.html).
