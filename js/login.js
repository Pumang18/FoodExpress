document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();
  verify();
});

function verify() {
  const email = document.getElementById("userEmail").value;
  const password = document.getElementById("userPassword").value;
  

  fetch("http://localhost:8080/auth/signin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email: email, password: password})
  })
    .then(response => {
      // Handle HTTP status codes with console logs
      if (response.status === 200) {
        return response.json();
      } else if (response.status === 404) {
        displayError("404 Not Found - User not found.");
      } else if (response.status === 401) {
        displayError("401 Unauthorized - Invalid credentials.");
      } else if (response.status === 500) {
        displayError("500 Internal Server Error - Please try again later.");
      } else {
        displayError("An unexpected error occurred.");
      }

    })
    .then(response => {
      if (!response) return;

      if (response.token){
        sessionStorage.setItem("token", response.token);}
      else
        throw new Error("Token Not Found");

      const userType = getUserType();
      if (userType == "Admin") {
        window.location.href = "../pages/admin/admin-home.html";
      } else if(userType == "Owner"){
        window.location.href = "../pages/owner/owner-home.html";
      }
      else{
        window.location.href = "../pages/customer/home-copy.html";
      }
    }
    )
    .catch(error => {
      console.log(error);
      document.getElementById("error").textContent = "Something went wrong. Please try again.";
    });

    function displayError(message) {
      let errorElem = document.getElementById("error");
      if (!errorElem) {
        errorElem = document.createElement("p");
        errorElem.id = "error";
        errorElem.classList.add("text-danger", "mt-3", "text-center");
        loginForm.parentElement.appendChild(errorElem);
      }
      errorElem.textContent = message;
    }
}