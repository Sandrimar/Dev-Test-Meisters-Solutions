let tasksData = [];
let currentPage = 1;
const tasksPerPage = 4;
let filteredTasksData = [];

document.addEventListener("DOMContentLoaded", () => {
    fetchTasks().then(() => renderTasks());
    document
        .getElementById("status-filter")
        .addEventListener("change", applyFilter);
});

function fetchTasks() {
    return fetch("http://localhost:8080/api/tasks")
        .then((response) => response.json())
        .then((tasks) => {
            tasksData = tasks;
            filteredTasksData = tasks;
            return tasks;
        })
        .catch((error) => {
            showErrorMessage(error, "");
            return [];
        });
}

function applyFilter() {
    const selectedStatus = document.getElementById("status-filter").value;
    const searchText = document
        .getElementById("search-input")
        .value.toLowerCase();

    filteredTasksData = tasksData.filter((task) => {
        const matchesStatus =
            selectedStatus === "ALL" || task.status === selectedStatus;
        const matchesSearchText =
            task.title.toLowerCase().includes(searchText) ||
            task.description.toLowerCase().includes(searchText);

        return matchesStatus && matchesSearchText;
    });

    currentPage = 1;
    renderTasks();
}

function createTaskHtml(task) {
    const taskDiv = document.createElement("div");
    taskDiv.className = "task";
    taskDiv.id = `${task.id}`;

    taskDiv.addEventListener("click", (event) => {
        if (event.target.tagName === "SELECT") {
            event.stopPropagation();
        } else {
            showEditPopupForm(task);
        }
    });

    taskDiv.innerHTML = `
    <header class="task-header">
      <h1 class="title">${task.title}</h1>
      <div class="status-and-button">
      <select class="status" id="status-${
          task.id
      }" onclick="event.stopPropagation()" onchange="handleStatusChange(${
        task.id
    })">
        <option value="IN_PROGRESS" ${
            task.status === "IN_PROGRESS" ? "selected" : ""
        }>IN PROGRESS</option>
          <option value="PENDING" ${
              task.status === "PENDING" ? "selected" : ""
          }>PENDING</option>
          <option value="COMPLETED" ${
              task.status === "COMPLETED" ? "selected" : ""
          }>COMPLETED</option>
        </select>
        <button class="delete-button" type="button" onclick="deleteTask(event, ${
            task.id
        })"><i class="fa-solid fa-trash"></i></button>
      </div>
    </header>
    <p class="description">${task.description}</p>
    <p class="date">${task.creationDate}</p>
  `;
    return taskDiv;
}

function renderTasks() {
    const taskList = document.querySelector(".task-list");
    taskList.innerHTML = "";

    if (filteredTasksData.length === 0) {
        const placeholder = document.createElement("div");
        placeholder.className = "placeholder";
        placeholder.textContent = "There are no tasks yet";
        taskList.appendChild(placeholder);

        document.getElementById("page-info").innerText = "0/0";
        document.getElementById("prev-page").disabled = true;
        document.getElementById("next-page").disabled = true;
    } else {
        const start = (currentPage - 1) * tasksPerPage;
        const end = start + tasksPerPage;
        const paginatedTasks = filteredTasksData.slice(start, end);

        paginatedTasks.forEach((task) => {
            const taskHtml = createTaskHtml(task);
            taskList.appendChild(taskHtml);
        });

        document.getElementById(
            "page-info"
        ).innerText = `${currentPage}/${Math.ceil(
            filteredTasksData.length / tasksPerPage
        )}`;

        document.getElementById("prev-page").disabled = currentPage === 1;
        document.getElementById("next-page").disabled =
            currentPage === Math.ceil(filteredTasksData.length / tasksPerPage);
    }
}

function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        renderTasks();
    }
}

function nextPage() {
    if (currentPage < Math.ceil(tasksData.length / tasksPerPage)) {
        currentPage++;
        renderTasks();
    }
}

document.getElementById("prev-page").addEventListener("click", prevPage);
document.getElementById("next-page").addEventListener("click", nextPage);

function showErrorMessage(error, message) {
    const errorMessage = document.getElementById("error-message");
    errorMessage.innerHTML = `<strong>${error}</strong>: ${message}`;
    errorMessage.style.display = "block";
    setTimeout(() => {
        errorMessage.style.display = "none";
    }, 5000);
}

function handleStatusChange(taskId) {
    const selectElement = document.getElementById(`status-${taskId}`);
    const newStatus = selectElement.value;

    updateTaskStatus(taskId, newStatus);
}

function createTask(event) {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;

    fetch("http://localhost:8080/api/tasks", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ title, description }),
    })
        .then((response) => {
            if (response.status === 201) {
                return response.json();
            } else {
                return response.json().then((error) => {
                    throw error;
                });
            }
        })
        .then((newTask) => {
            tasksData.push(newTask);
            applyFilter();
            showSuccessMessage();
            document.getElementById("new-task-form").reset();
        })
        .catch((error) => {
            showErrorMessage(error.error, error.message);
        });
    hidePopupForm();
}

function updateTaskStatus(taskId, newStatus) {
    const originalTask = tasksData.find((task) => task.id === taskId);
    const originalStatus = originalTask.status;

    fetch(`http://localhost:8080/api/tasks/${taskId}?status=${newStatus}`, {
        method: "PATCH",
    })
        .then((response) => {
            if (!response.ok) {
                return response.json().then((err) => {
                    throw new Error(err.message);
                });
            }
            originalTask.status = newStatus;
            applyFilter();
        })
        .catch((error) => {
            document.getElementById(`status-${taskId}`).value = originalStatus;
            showErrorMessage("Erro ao atualizar status", error.message);
        });
}

function deleteTask(event, taskId) {
    event.stopPropagation();

    fetch(`http://localhost:8080/api/tasks/${taskId}`, { method: "DELETE" })
        .then((response) => {
            if (response.ok) {
                tasksData = tasksData.filter((task) => task.id !== taskId);
                applyFilter();
                renderTasks(currentPage);
            } else {
                return response.json().then((error) => {
                    throw error;
                });
            }
        })
        .catch((error) => {
            showErrorMessage(error.error, error.message);
        });
}

function showPopupForm() {
    document.getElementById("popup-form").style.display = "block";
    document.querySelector(".container").classList.add("blurred");
}

function hidePopupForm() {
    document.getElementById("popup-form").style.display = "none";
    document.querySelector(".container").classList.remove("blurred");
}

function showSuccessMessage() {
    const successMessage = document.getElementById("success-message");
    successMessage.style.display = "block";
    setTimeout(() => {
        successMessage.style.display = "none";
    }, 3000);
}

document
    .getElementById("add-task-button")
    .addEventListener("click", showPopupForm);

document
    .getElementById("cancel-button")
    .addEventListener("click", hidePopupForm);

document.getElementById("new-task-form").addEventListener("submit", createTask);

function showEditPopupForm(task) {
    document.getElementById("edit-task-id").value = task.id;
    document.getElementById("edit-title").value = task.title;
    document.getElementById("edit-description").value = task.description;
    document.getElementById("edit-popup-form").style.display = "block";
    document.querySelector(".container").classList.add("blurred");
}

function hideEditPopupForm() {
    document.getElementById("edit-popup-form").style.display = "none";
    document.querySelector(".container").classList.remove("blurred");
}

document
    .getElementById("edit-cancel-button")
    .addEventListener("click", hideEditPopupForm);

function updateTask(event) {
    event.preventDefault();

    const id = document.getElementById("edit-task-id").value;
    const title = document.getElementById("edit-title").value;
    const description = document.getElementById("edit-description").value;

    fetch(`http://localhost:8080/api/tasks/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ title, description }),
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then((error) => {
                    throw error;
                });
            }
        })
        .then((updatedTask) => {
            const index = tasksData.findIndex(
                (task) => task.id === updatedTask.id
            );
            tasksData[index] = updatedTask;
            renderTasks(currentPage);
            showSuccessMessage();
        })
        .catch((error) => {
            showErrorMessage(error.error, error.message);
        });
    hideEditPopupForm();
}

document
    .getElementById("edit-task-form")
    .addEventListener("submit", updateTask);

document.getElementById("search-input").addEventListener("input", applyFilter);

document.getElementById("clear-search-button").addEventListener("click", () => {
    document.getElementById("search-input").value = "";
    applyFilter();
});

function showEmailPopupForm() {
    document.getElementById("send-email-popup").style.display = "block";
    document.querySelector(".container").classList.add("blurred");
}

function hideEmailPopupForm() {
    document.getElementById("send-email-popup").style.display = "none";
    document.querySelector(".container").classList.remove("blurred");
}

document
    .getElementById("send-email")
    .addEventListener("click", showEmailPopupForm);

document
    .getElementById("email-cancel-button")
    .addEventListener("click", hideEmailPopupForm);

document
    .getElementById("send-email-form")
    .addEventListener("submit", sendEmail);

function sendEmail(event) {
    event.preventDefault();

    const email = document.getElementById("email").value;
    fetch(`http://localhost:8080/api/email/send?to=${email}`, {
        method: "POST",
    })
        .then((response) => {
            if (response.ok) {
                showSuccessMessage();
                document.getElementById("send-email-form").reset();
            }
        })
        .catch((error) => {
            showErrorMessage(error, "");
        });
    hideEmailPopupForm();
}
