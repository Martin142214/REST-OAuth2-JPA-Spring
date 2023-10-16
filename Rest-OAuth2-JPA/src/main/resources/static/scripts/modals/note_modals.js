function openNoteAddModal(patientId, patientFirstName, patientLastName, doctorId, patientImagePath) {
    document.getElementById('id01').style.display='block';
    document.getElementById('patientNameTitle').innerText = patientFirstName + ' ' + patientLastName;
    document.getElementById('doctorIdInputHidden').value = doctorId;
    document.getElementById('patientImage').src = patientImagePath;

    document.getElementById('addNotesModalForm').setAttribute("action", "/user/patient/" + patientId + "/notes/add");
}

function openNotesListModal(patientFullName, patientNotes) {
    document.getElementById('id02').style.display='block';
    const notesObject = [];
    document.getElementById("modalDivTitle").innerHTML = `${patientFullName}'s notes`;

    var modalDiv = document.getElementById('modalDiv');

    patientNotes.forEach(note => {
        notesObject.push({
           "id": note.id,
           "data": note.data,
        });
        while (notesObject.length < patientNotes.length) {
            return;
        }
    });
    initializeNotesList(modalDiv, notesObject);

    /*var h3_title = document.createElement("h3");
    h3_title.innerText = `${userPatient.patient.personalInfo.firstName} ${userPatient.patient.personalInfo.lastName}'s notes`;

    var notesContainerDiv = document.createElement("div");
    notesContainerDiv.classList.add("notesContainer");

    var formElement = document.createElement("form");
    formElement.name = "notesForm";
    formElement.action = `/user/patient/notes/delete/${note.id}`;*/

}

function initializeNotesList(modalDiv, notesObject){
    let notes = "";

    notesObject.forEach(note => {
        notes += `  
                <div class="notesContainer">
                    <form name="notesForm" action="/user/patient/notes/delete/${note.id}" method="post">
                        <input type="submit" class="time-right" value="&times;" style="cursor: pointer; border: none;"></input>
                    </form>   
                    <span style="font-size: 20px; font-weight: bold;">Up to date patient diagnosis: ${note.data.diagnosis}</span>
                    <p>${note.data.description}</p>
                    <span class="time-right">Date: ...</span>  
                </div>
                `
    });

    modalDiv.innerHTML = notes;
}