document.addEventListener("DOMContentLoaded", () => {
  const restaurantList = document.getElementById("restaurantList");

  // Fetch data from db.json
  fetch("http://localhost:3000/restaurants")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      const restaurants = data;

      // Clear existing content
      if (restaurantList) {
        restaurantList.innerHTML = "";

        // Create and append restaurant cards
        restaurants.forEach((restaurant) => {
          const card = `
              <div class="col-md-4 mb-4">
                <div class="card h-100">
                  <img src="" class="card-img-top" alt="${
            restaurant.name
          }">
                  <div class="card-body">
                    <h5 class="card-title">${restaurant.name}</h5>
                    <p class="card-text mb-1">
                      <i class="fas fa-map-marker-alt text-primary me-1"></i> ${
                        restaurant.location
                      }
                    </p>
                    <a href="restaurant.html?id=${restaurant.restaurantId
                    }" class="btn btn-primary w-100">View Menu</a>
                  </div>
                </div>
              </div>
            `;
          restaurantList.innerHTML += card;
        });
      }

      // Initialize cart count
      const cartCount = localStorage.getItem("cartCount") || 0;
      document.querySelectorAll(".cart-count").forEach((el) => {
        el.textContent = cartCount;
      });
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
      restaurantList.innerHTML =
        "<p class='text-danger'>Failed to load restaurants.</p>";
    });
});
