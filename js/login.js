document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();
  verify();
});

const apiUrl = "http://localhost:8080/auth";

document.getElementById("forgotPasswordForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const email = document.getElementById("email").value;

  try {
    const response = await fetch(`${apiUrl}/forgot-password`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },

      body: JSON.stringify({ email }),
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      alert(errorMessage);
      return;
    }

    alert("OTP sent to your email.");
    document.getElementById("otpSection").classList.remove("d-none");
  } catch (err) {
    console.error("Error sending OTP:", err);
    alert("Failed to send OTP. Please try again.");
  }
});

document.getElementById("verifyOtpForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const email = document.getElementById("email").value;
  const otp = document.getElementById("otp").value;
  const newPassword = document.getElementById("newPassword").value;

  try {
    const response = await fetch(`${apiUrl}/verify-otp`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, otp }),
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      alert(errorMessage);
      return;
    }

    // Reset the password
    const resetResponse = await fetch(`${apiUrl}/reset-password`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, newPassword }),
    });

    if (!resetResponse.ok) {
      const errorMessage = await resetResponse.text();
      alert(errorMessage);
      return;
    }

    alert("Password reset successfully!");
    const modal = bootstrap.Modal.getInstance(
      document.getElementById("forgotPasswordModal")
    );
    modal.hide();
  } catch (err) {
    console.error("Error resetting password:", err);
    alert("Failed to reset password. Please try again.");
  }
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