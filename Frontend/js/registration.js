apiUrl = "http://localhost:3000/users";

let form = document.getElementById("registrationForm");
let userName = document.getElementById("userName");
let userEmail = document.getElementById("userEmail");
let userPassword = document.getElementById("userPassword");
let userPhone = document.getElementById("userPhone");
let userAddress = document.getElementById("userAddress");
let userUserType = document.getElementById("userType");
let userTypeError = document.getElementById("resultBox");
let emailExist = document.getElementById("emailExist")
let users = [];

function validateName() {
  if (userName.value.trim().length >= 5) {
    userName.classList.add("is-valid");
    userName.classList.remove("is-invalid");
    return true;
  } else {
    userName.classList.add("is-invalid");
    userName.classList.remove("is-valid");
    return false;
  }
}
userName.addEventListener("blur", validateName);

function validatePassword() {
  const passVal = userPassword.value;
  const passRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,}$/;
  if (passRegex.test(passVal)) {
    userPassword.classList.add("is-valid");
    userPassword.classList.remove("is-invalid");
    return true;
  } else {
    userPassword.classList.add("is-invalid");
    userPassword.classList.remove("is-valid");
    return false;
  }
}

userPassword.addEventListener("blur", validatePassword);

function validatePhone() {
  const mobileRegex = /^[789]\d{9}$/;
  if (mobileRegex.test(userPhone.value.trim())) {
    userPhone.classList.add("is-valid");
    userPhone.classList.remove("is-invalid");
    return true;
  } else {
    userPhone.classList.add("is-invalid");
    userPhone.classList.remove("is-valid");
    return false;
  }
}

userPhone.addEventListener("blur", validatePhone);

function validateAddress() {
  console.log("IN ADDRESS V");
  if (userAddress.value.trim().length >= 5) {
    userAddress.classList.add("is-valid");
    userAddress.classList.remove("is-invalid");
    return true;
  } else {
    userAddress.classList.add("is-invalid");
    userAddress.classList.remove("is-valid");
    return false;
  }
}

userAddress.addEventListener("blur", validateAddress);

function validateUserType() {
  const userType = document.querySelector("option");
  if (!userType) {
    userTypeError.classList.remove("d-none");
    return false;
  } else {
    userTypeError.classList.add("d-none");
    return true;
  }
}
userUserType.addEventListener("blur", validateUserType);

async function fetchUsers() {
  await fetch(apiUrl)
    .then((res) => res.json())
    .then((data) => {
      users = data;
    });
}

fetchUsers();
let isPresent = false;
form.addEventListener("submit", function (e) {
  e.preventDefault();

  const user = {
    name: userName.value.trim(),
    email: userEmail.value.trim(),
    password: userPassword.value,
    phone: userPhone.value.trim(),
    address: userAddress.value.trim(),
    userType: userUserType.value.trim(),
  };

  users.forEach((u) => {
    if (u.email === user.email) {
      emailExist.classList.remove("d-none")
      isPresent = true;
    }
  });
  if (!isPresent) {
    fetch(apiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    }).then(form.reset());

    window.location.href = "login.html";
  }
});

function clearValidation() {
  userName.classList.remove("is-valid", "is-invalid");
  userEmail.classList.remove("is-valid", "is-invalid");
  userPassword.classList.remove("is-valid", "is-invalid");
  userPhone.classList.remove("is-valid", "is-invalid");
  userAddress.classList.remove("is-valid", "is-invalid");
  userUserType.classList.remove("is-valid", "is-invalid");
}
