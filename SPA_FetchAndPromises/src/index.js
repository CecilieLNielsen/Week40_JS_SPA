import 'bootstrap/dist/css/bootstrap.css'

// Opsætning
const base_url = 'http://localhost:3333/api/users/';

function fetchWithErrorCheck(res) {
  if (!res.ok) {
    return Promise.reject({ status: res.status, fullError: res.json() })
  }
  return res.json();
}

// Opgave 1
function fetchAllUsers() {
  fetch(base_url)
    .then(res => fetchWithErrorCheck(res))
    .then(data => {
      const tableRows = data.map(user => {
        return `<tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.age}</td>
            <td>${user.gender}</td>
            <td>${user.email}</td>
          </tr>`
      }).join('');
      document.getElementById('tbody').innerHTML = tableRows;
    });
}

fetchAllUsers();

// Opgave 2
function fetchUser() {
  const userID = document.getElementById("inputId").value;
  fetch(base_url + userID)
    .then(res => fetchWithErrorCheck(res))
    .then(user => {
      const userData = `${user.id}: ${user.name}, ${user.age}, ${user.gender}, ${user.email}`;
      document.getElementById("singleUser").innerHTML = userData;
    });
}

document.getElementById("fetchUserBtn").addEventListener("click", function (event) {
  event.preventDefault();
  fetchUser();
});

// Opgave 3
function addUser() {
  // Get values
  const name = document.getElementById("userName").value;
  const age = document.getElementById("userAge").value;
  const gender = document.getElementById("userGender").value;
  const email = document.getElementById("userEmail").value;

  // Reset input fields
  document.getElementById("userName").value = '';
  document.getElementById("userAge").value = '';
  document.getElementById("userGender").value = '';
  document.getElementById("userEmail").value = '';

  // Defined options for fetch
  let options = {
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      name,
      age,
      gender,
      email
    })
  }

  fetch(base_url, options)
    .then(res => fetchWithErrorCheck(res))
    .then(() => {
      fetchAllUsers();
    });
}

document.getElementById("addUserBtn").addEventListener("click", function (event) {
  event.preventDefault();
  addUser();
});

// Opgave 4
function editUser() {
  // Get values
  const id = document.getElementById("editId").value;
  const name = document.getElementById("editName").value;
  const age = document.getElementById("editAge").value;
  const gender = document.getElementById("editGender").value;
  const email = document.getElementById("editEmail").value;

  // Reset input fields
  document.getElementById("editId").value = '';
  document.getElementById("editName").value = '';
  document.getElementById("editAge").value = '';
  document.getElementById("editGender").value = '';
  document.getElementById("editEmail").value = '';

  // Defined options for fetch
  let options = {
    method: "PUT",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      name,
      age,
      gender,
      email
    })
  }

  fetch(base_url + id, options)
    .then(res => fetchWithErrorCheck(res))
    .then(() => {
      fetchAllUsers();
    });
}

document.getElementById("editUserBtn").addEventListener("click", function (event) {
  event.preventDefault();
  editUser();
});

// Opgave 5
function deleteUser() {
  // Get values
  const id = document.getElementById("deleteId").value;

  // Reset input fields
  document.getElementById("deleteId").value = '';

  // Defined options for fetch
  let options = {
    method: "DELETE"
  }

  fetch(base_url + id, options)
    .then(res => fetchWithErrorCheck(res))
    .then(() => {
      fetchAllUsers();
    });
}

document.getElementById("deleteUserBtn").addEventListener("click", function (event) {
  event.preventDefault();
  deleteUser();
});