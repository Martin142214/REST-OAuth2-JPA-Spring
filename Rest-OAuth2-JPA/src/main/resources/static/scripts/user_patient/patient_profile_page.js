function redirectSelect(element)
  {
     window.location = element.value;
  }

function setColorToPatientStatus() {
   var pTag = document.getElementById("patientStatus");
   pTag.style.borderRadius = "5px";
   pTag.style.color = "white";
   var status = pTag.innerHTML.toLowerCase();
   switch (status) {
      case "unknown":
         pTag.style.backgroundColor = 'grey';
         break;
      case "healthy":
         pTag.style.backgroundColor = 'green';
         break;
      case "sick":
         pTag.style.backgroundColor = 'red';
         break;
      case "in_treatment":
         pTag.style.backgroundColor = 'orange';
         break;
      
   }
}