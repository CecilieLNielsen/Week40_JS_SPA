import 'bootstrap/dist/css/bootstrap.css'

// Opsætning
const base_url = 'https://www.bycecilie.dk/CORS_Backend/api/person/';

function fetchWithErrorCheck(res) {
  if (!res.ok) {
    return Promise.reject({ status: res.status, fullError: res.json() })
  }
  return res.json();
}

// Opgave 1
function fetchAllUsers() {
  fetch(base_url + 'all')
    .then(res => fetchWithErrorCheck(res))
    .then(data => {
      const users = data.all;
      const tableRows = users.map(user => {
        return `<tr>
            <td>${user.id}</td>
            <td>${user.firstname} ${user.lastname}</td>
            <td>${user.phone}</td>
            <td>${user.street}</td>
            <td>${user.zip} ${user.city}</td>
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
      const userData = `${user.id}: ${user.firstname} ${user.lastname}, ${user.phone}, ${user.street}, ${user.zip} ${user.city}`;
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
  const firstname = document.getElementById("userFirstname").value;
  const lastname = document.getElementById("userLastname").value;
  const phone = document.getElementById("userPhone").value;
  const street = document.getElementById("userStreet").value;
  const zip = document.getElementById("userZip").value;
  const city = document.getElementById("userCity").value;

  // Reset input fields
  document.getElementById("userFirstname").value = '';
  document.getElementById("userLastname").value = '';
  document.getElementById("userPhone").value = '';
  document.getElementById("userStreet").value = '';
  document.getElementById("userZip").value = '';
  document.getElementById("userCity").value = '';

  // Defined options for fetch
  let options = {
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      firstname,
      lastname,
      phone,
      street,
      zip,
      city
    })
  }

  fetch(base_url + 'add', options)
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
  const firstname = document.getElementById("editFirstname").value;
  const lastname = document.getElementById("editLastname").value;
  const phone = document.getElementById("editPhone").value;
  const street = document.getElementById("editStreet").value;
  const zip = document.getElementById("editZip").value;
  const city = document.getElementById("editCity").value;

  // Reset input fields
  document.getElementById("editId").value = '';
  document.getElementById("editFirstname").value = '';
  document.getElementById("editLastname").value = '';
  document.getElementById("editPhone").value = '';
  document.getElementById("editStreet").value = '';
  document.getElementById("editZip").value = '';
  document.getElementById("editCity").value = '';

  // Defined options for fetch
  let options = {
    method: "PUT",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      firstname,
      lastname,
      phone,
      street,
      zip,
      city
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