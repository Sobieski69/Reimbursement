let iCnt = 1;
var dropdownValues = getDropdownList();
var numberOfListElements = dropdownValues.length;
let xhr = new XMLHttpRequest();

function addRowClient(limit) {
  if(iCnt >= 1){
    var tableHeaders = document.getElementById('tableHeaders');
    tableHeaders.style.display = "table-row";
  }

  const button1 = document.getElementById("addButton");
  if (iCnt == limit) {
    button1.disabled = "true";
  }
  if (iCnt <= limit) {
    let myTable = document.getElementById("tab");
    let rowCnt = myTable.rows.length;
    let tr = myTable.insertRow(rowCnt);
    let headCount = myTable.rows[1].getElementsByTagName("td").length;

    for (let c = 0; c < headCount; c++) {
      let td = document.createElement("td");
      td.setAttribute("class", "tdd");
      td = tr.insertCell(c);
      if (c === 0) {
        createSelect(td);
      } else {
        createInputNumber("selectValue", td);
      }
    }
    iCnt += 1;
  }
}

function addRowAdmin(limit) {
  const button1 = document.getElementById("addButton");
  if (iCnt == limit) {
    button1.disabled = "true";
  }
  if (iCnt <= limit) {
    let myTable = document.getElementById("tab");
    let rowCnt = myTable.rows.length;
    let tr = myTable.insertRow(rowCnt);
    let headCount = myTable.rows[1].getElementsByTagName("td").length;

    for (let c = 0; c < headCount; c++) {
      let td = document.createElement("td");
      td.setAttribute("class", "tdd");
      td = tr.insertCell(c);
      if (c === 0) {
        createInputString("inputString", td);
      } else {
        createInputNumber("limitValue", td);
      }
    }
    iCnt += 1;
  }
}

function createJsonClient() {
  let mileage = document.getElementById("mileage").value;
  let days = document.getElementById("days").value;
  if (mileage == "" || days == "") {
    printResponseMessage("errorMessage","Please fill every field.");
    return 0;
  }else if(mileage<0 || days<0){
    printResponseMessage("errorMessage", "Value must be a positive number.");
    return 0;
  }else if(days.includes(",") || days.includes(".")){
    printResponseMessage("errorMessage","Invalid data");
    return 0;
  }

  let selectValues = [];
  let selectTypes = [];

        var hasDuplicate;
        var duplicateIndex;
  for (var i = 1; i < iCnt; i++) {
      hasDuplicate = false;
      var selectValueSource = document.getElementById("selectValue" + i).value;
      var receiptTypeSource = document.getElementById("select" + i).value;
      for(var j=0; j<selectValues.length; j++){
        if(receiptTypeSource === selectTypes[j]){
            duplicateIndex = j;
            console.log(j);
            hasDuplicate = true;
            selectValues[j] = parseInt(selectValueSource) + parseInt(selectValues[j]);
        }
      }

      if (selectValueSource === "" || hasDuplicate === true) {
        continue;
      }else if(selectValueSource<0){
        printResponseMessage("errorMessage", "Value must be a positive number.");
        return 0;
      } else {
          selectValues.push(selectValueSource);
          selectTypes.push(receiptTypeSource);
      }
    }
  var receipts = [];

  for (var i = 0; i < selectValues.length; i++) {
        receipts.push({ receiptType: selectTypes[i], value: selectValues[i]});
  }

  var json = { mileage: mileage, days: days, receipts: receipts };

  return json;
}

function createJsonAdmin() {
  let mileageRate = document.getElementById("mileageRate").value;
  let dailyAllowance = document.getElementById("dailyAllowance").value;
  if (mileageRate == "" || dailyAllowance== "") {
    printResponseMessage("errorMessage","Please fill every field.");
    return 0;
  }

  let limitValues = [];
  let receiptTypes = [];

  for (var i = 1; i < iCnt; i++) {
    var limitValueSource = document.getElementById("limitValue" + i).value;
    var receiptTypeSource = document.getElementById("inputString" + i).value;

    if (receiptTypeSource == "") {
      continue;
    } else {
        receiptTypes.push(receiptTypeSource);
        limitValues.push(limitValueSource);
    }
  }
  var receipts = [];
  for (var i = 0; i < receiptTypes.length; i++) {

    if (receiptTypes[i] == undefined || receiptTypes[i] == "") {
      continue;
    } else {
      receipts.push({ receiptType: receiptTypes[i], value: limitValues[i] });

    }
  }

  if(receiptTypes.length == 0){
    printResponseMessage("errorMessage","Pleas fill at least one receipt");
    return 0;
  }
  var json = {
    mileageRate: mileageRate,
    dailyAllowance: dailyAllowance,
    receiptParams: receipts,
  };
  return json;
}

function sendJSON() {
   var clientJson = createJsonClient();
    if (!clientJson == 0) {
      send("POST", "data", clientJson);
      handleResponse();
    }

}

function sendAdminUpdate() {
  var adminJson = createJsonAdmin();
  if (!adminJson == 0) {
    send("PATCH", "admin", adminJson);
    handleResponse();
  }

}

function send(requestType, endpoint, data) {
  let url = "http://localhost:8080/" + endpoint.toString();

  xhr.open(requestType.toString(), url, false);

  xhr.setRequestHeader("values", "application/json");

  xhr.send(JSON.stringify(data));

//  var response = xhr.response;
//  printResponseMessage(response);
}



function createSelect(td) {
  let span = document.createElement("span");
  var options = [];
  td.appendChild(span);
  let ele = document.createElement("select");
  ele.setAttribute("id", "select" + iCnt.toString());
  for (var i = 0; i < numberOfListElements; i++) {
    options.push(new Option(dropdownValues[i]));
    ele.add(options[i]);
  }
  td.appendChild(ele);
}

function createInputString(elementId, td) {
  let value = document.createElement("input");
  value.setAttribute("type", "text");
  value.setAttribute("value", "");
  value.setAttribute("id", elementId.toString() + iCnt.toString());
  td.appendChild(value);
}

function createInputNumber(elementId, td) {
  let value = document.createElement("input");
  value.setAttribute("type", "number");
  value.setAttribute("value", "");
  value.setAttribute("min", 0);
  value.setAttribute("id", elementId.toString() + iCnt.toString());
  td.appendChild(value);
}

function getDropdownList() {
  let url = "http://localhost:8080/receiptMenu";
  let xhr1 = new XMLHttpRequest();
  xhr1.open("GET", url, false);
  xhr1.send();
  var payload = xhr1.response;
  var payloadJson = JSON.parse(payload);
  return payloadJson.data;
}


function printResponseMessage(responseType, responseMessage) {
  var messageBox = document.getElementById("message");
  messageBox.setAttribute('class', responseType);
  messageBox.innerHTML = responseMessage;
}

function handleResponse() {
    responseJson = JSON.parse(xhr.response);
    switch(responseJson.responseType){
        case 'clientSuccess':
            printResponseMessage("successMessage",'Your claim: '+ responseJson.data + '$');
            break;
        case 'adminSuccess':
            printResponseMessage("successMessage", responseJson.data[0]);
            break;
        case 'error':
            printResponseMessage("errorMessage", responseJson.data);
            break;
    }
}
