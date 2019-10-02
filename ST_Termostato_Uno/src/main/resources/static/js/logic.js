var digitalTwinApi = Java.type('com.minsait.onesait.platform.digitaltwin.logic.api.DigitalTwinApi').getInstance();
var ObjectMapper = Java.type('com.fasterxml.jackson.databind.ObjectMapper');
var objectMapper = new ObjectMapper();
var HashMap = Java.type('java.util.HashMap');

function init(){
  digitalTwinApi.log('Init Termostato');
}

function main(){
    digitalTwinApi.log('Main Termostato');
    // Convertimos los datos a un Map, luego lo pasamos por Jackson para convertirlo en un String con el JSON para realizar el guardado.
    var hm = new HashMap();
    var temperatura = Math.round(getRandom(23.01, 26.99) * 100) / 100;
    digitalTwinApi.setStatusValue("temperatura", temperatura)
    hm.put('temperatura', temperatura);
    digitalTwinApi.sendUpdateShadow(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hm));
    digitalTwinApi.log('Set updateShadow');
}

/**
 * Obtiene un número aleatorio comprendido entre min y max.
 */
function getRandom(min, max) {
    return Math.random() * (max - min) + min;
}

var onActionObtenerTemperatura=function(data){ }
