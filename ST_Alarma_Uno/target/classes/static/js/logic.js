var digitalTwinApi = Java.type('com.minsait.onesait.platform.digitaltwin.logic.api.DigitalTwinApi').getInstance();
function init(){
  // Escribimos en el log.
  digitalTwinApi.log("Init");
  // Inicializamos el estado a false.
  digitalTwinApi.setStatusValue("isOn", false);
}
function main(){
}

var onActionSetActivacion=function(data){ 
    digitalTwinApi.log("onActionSetActivacion: " + data);
}
