# HTTP-basierte Stromzähler

Richtige Stromzähler, deren Werte man per HTTP abfragen kann, gibt es meines Wissens nicht. Wenn man allerdings andere Hausautomatisierungen (z.B. FHEM) verwendet, kann man dort eingebundene Stromzähler via HTTP abfragen.

## Beispiel Stromverbrauchsmessung über Fritz!Box Home Automation
Die Fritz!Box erlaubt die Abfrage der über eine Steckdose entnommenen Leistung via HTTP, wobei der Wert in mW geliefert wird (deshalb muss ```factorToWatt="1000"``` gesetzt werden, eil SAE eine Angabe in W erwartet).
Die Konfiguration dafür würde wie folgt aussehen, wobei die Session ID (ain) über ein zwischengeschaltetes Script gesetzt werden müsste:
```
<?xml version="1.0" encoding="UTF-8"?>
<Appliances xmlns="http://github.com/camueller/SmartApplianceEnabler/v1.1">
    <Appliance id="F-00000001-000000000001-00">
        <HttpElectricityMeter url="http://192.168.2.1/webservices/homeautoswitch.lua?ain=xxx&switchcmd=getswitchpower" factorToWatt="1000" />
    </Appliance>
</Appliances>
```
Optional können folgende Parameter gesetzt werden:
- measurementInterval in Sekunden (default=60) : Zeitraum, für den der durchschnittliche Verbrauch berechnet wird
- pollInterval in Sekunden (default=10) : die Zeit zwischen zwei Verbrauchsabfragen beim Zähler

Allgemeine Hinweise zu diesem Thema finden sich im Kapitel [Konfiguration](Configuration_DE.md).