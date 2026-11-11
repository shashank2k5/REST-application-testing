
const apiUrl = "/api/students";

const form = document.getElementById("studentForm");
const list = document.getElementById("studentList");
const message = document.getElementById("message");

const submitBtn = document.getElementById("submitBtn");
const cancelBtn = document.getElementById("cancelBtn");

let editingId = null;


// GET: Load all students
async function loadStudents() {

    try {

        const response = await fetch(apiUrl);

        if (!response.ok) {
            throw new Error("Unable to load students");
        }

        const students = await response.json();

        list.innerHTML = "";

        students.forEach(student => {

            const item = document.createElement("li");

            // Student details
            const details = document.createElement("div");

            details.className = "student-details";

            details.textContent =
                `${student.id} - ${student.name} - ` +
                `${student.email} - ${student.department} - ` +
                `Age: ${student.age}`;

            // Action buttons container
            const actions = document.createElement("div");

            actions.className = "action-buttons";

            // EDIT BUTTON
            const editButton = document.createElement("button");

            editButton.textContent = "Edit";
            editButton.className = "edit-btn";

            editButton.addEventListener("click", () => {
                editStudent(student);
            });

            // DELETE BUTTON
            const deleteButton = document.createElement("button");

            deleteButton.textContent = "Delete";
            deleteButton.className = "delete-btn";

            deleteButton.addEventListener("click", () => {
                deleteStudent(student.id);
            });

            // Append buttons
            actions.appendChild(editButton);
            actions.appendChild(deleteButton);

            item.appendChild(details);
            item.appendChild(actions);

            list.appendChild(item);

        });

    } catch (error) {

        message.textContent = error.message;

    }
}


// EDIT: Fill form with existing student details
function editStudent(student) {

    editingId = student.id;

    document.getElementById("name").value = student.name;
    document.getElementById("email").value = student.email;
    document.getElementById("department").value = student.department;
    document.getElementById("age").value = student.age;

    submitBtn.textContent = "Update Student";

    cancelBtn.hidden = false;

    message.textContent = "Editing student ID: " + student.id;

    form.scrollIntoView({
        behavior: "smooth"
    });

}


// CREATE / UPDATE
form.addEventListener("submit", async event => {

    event.preventDefault();

    const student = {

        name: document.getElementById("name").value.trim(),

        email: document.getElementById("email").value.trim(),

        department: document.getElementById("department").value.trim(),

        age: Number(document.getElementById("age").value)

    };

    try {

        const isEditing = editingId !== null;

        const url = isEditing
            ? `${apiUrl}/${editingId}`
            : apiUrl;

        const method = isEditing ? "PUT" : "POST";

        const response = await fetch(url, {

            method: method,

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(student)

        });

        if (!response.ok) {

            let errorMessage = "Request failed";

            try {
                const error = await response.json();

                errorMessage =
                    error.message ||
                    JSON.stringify(error.messages || error);

            } catch {
                errorMessage = `Request failed: ${response.status}`;
            }

            throw new Error(errorMessage);

        }

        message.textContent = isEditing
            ? "Student updated successfully!"
            : "Student added successfully!";

        // Reset form
        form.reset();

        editingId = null;

        submitBtn.textContent = "Save Student";

        cancelBtn.hidden = true;

        // Refresh list
        await loadStudents();

    } catch (error) {

        message.textContent = error.message;

    }

});


// CANCEL EDIT
cancelBtn.addEventListener("click", () => {

    form.reset();

    editingId = null;

    submitBtn.textContent = "Save Student";

    cancelBtn.hidden = true;

    message.textContent = "Edit cancelled.";

});


// DELETE STUDENT
async function deleteStudent(id) {

    const confirmed = confirm(
        "Are you sure you want to delete this student?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(`${apiUrl}/${id}`, {

            method: "DELETE"

        });

        if (!response.ok) {
            throw new Error("Unable to delete student");
        }

        message.textContent = "Student deleted successfully!";

        // If currently editing this student, reset form
        if (editingId === id) {

            form.reset();

            editingId = null;

            submitBtn.textContent = "Save Student";

            cancelBtn.hidden = true;

        }

        await loadStudents();

    } catch (error) {

        message.textContent = error.message;

    }

}


// INITIAL LOAD
loadStudents();
