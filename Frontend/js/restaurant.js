document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = parseInt(urlParams.get('id'));
    console.log(restaurantId)

    let foodItemsData, restaurantsData;
    try {
        const [foodRes, restRes] = await Promise.all([
            fetch('http://localhost:3000/foodItems'),
            fetch('http://localhost:3000/restaurants')
        ]);
        foodItemsData = await foodRes.json();
        restaurantsData = await restRes.json();
    } catch (error) {
        console.error('Error fetching data:', error);
        return;
    }
    const currentRestaurant = restaurantsData[restaurantId-1];
    console.log(currentRestaurant)
    if (!currentRestaurant) return;

    // Update restaurant info
    document.getElementById('restaurant-name').textContent = currentRestaurant.name;
    document.getElementById('restaurant-title').textContent = currentRestaurant.name;
    document.getElementById('restaurant-location').textContent = currentRestaurant.location;
    document.getElementById('restaurant-contact').textContent = currentRestaurant.contact;

    // Filter food items by restaurant
    const filteredItems = foodItemsData.filter(item => item.restaurantId === restaurantId);

    const categories = ['appetizers', 'main-courses', 'desserts', 'drinks'];
    categories.forEach(category => {
        const categoryItems = filteredItems.filter(item => item.category === category);
        const container = document.getElementById(`${category}-list`);
        if (!container) return;

        container.innerHTML = '';
        categoryItems.forEach(item => {
            const itemCard = `
                <div class="col-md-4 mb-4">
                  <div class="card h-100 shadow-sm">
                    <div class="card-body">
                      <h5 class="card-title">${item.name}</h5>
                      <p class="card-text text-muted">Price: ₹${item.price}</p>
                      <div class="input-group mb-3">
                        <button class="btn btn-outline-secondary" type="button" onclick="decreaseQty(this)">−</button>
                        <input type="text" class="form-control text-center" value="1" readonly />
                        <button class="btn btn-outline-secondary" type="button" onclick="increaseQty(this)">+</button>
                      </div>
                      <button class="btn btn-primary w-100 add-to-cart"
                              data-id="${item.itemId}"
                              data-name="${item.name}"
                              data-price="${item.price}">
                        Add to Cart
                      </button>
                    </div>
                  </div>
                </div>
            `;
            container.innerHTML += itemCard;
        });
    });

    // Initialize cart
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    updateCartCount();

    // Event delegation for "Add to Cart"
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', function () {
            const itemId = this.getAttribute('data-id');
            const itemName = this.getAttribute('data-name');
            const itemPrice = parseFloat(this.getAttribute('data-price'));

            const card = this.closest('.card');
            const quantity = parseInt(card.querySelector('input').value);

            const existingItem = cart.find(item => item.id === itemId);

            if (existingItem) {
                existingItem.quantity += quantity;
            } else {
                cart.push({
                    id: itemId,
                    name: itemName,
                    price: itemPrice,
                    quantity: quantity,
                    restaurant: currentRestaurant.name
                });
            }

            localStorage.setItem('cart', JSON.stringify(cart));
            updateCartCount();

            this.innerHTML = '<i class="fas fa-check"></i> Added';
            this.classList.remove('btn-primary');
            this.classList.add('btn-success');
            setTimeout(() => {
                this.innerHTML = 'Add to Cart';
                this.classList.remove('btn-success');
                this.classList.add('btn-primary');
            }, 1500);
        });
    });

    function updateCartCount() {
        const totalItems = cart.reduce((total, item) => total + item.quantity, 0);
        localStorage.setItem('cartCount', totalItems);
        document.querySelectorAll('.cart-count').forEach(el => {
            el.textContent = totalItems;
        });
    }
});

// Quantity functions
function increaseQty(btn) {
    const input = btn.parentElement.querySelector('input');
    input.value = parseInt(input.value) + 1;
}
function decreaseQty(btn) {
    const input = btn.parentElement.querySelector('input');
    if (parseInt(input.value) > 1) {
        input.value = parseInt(input.value) - 1;
    }
}


// document.addEventListener('DOMContentLoaded', async () => {
//     const urlParams = new URLSearchParams(window.location.search);
//     const restaurantId = urlParams.get('id') || 'pizza-palace';

//     let data;
//     try {
//         const res = await fetch('http://localhost:3000/foodItems');
//         data = await res.json();
//     } catch (error) {
//         console.error('Error fetching data:', error);
//         return;
//     }

//     // const currentRestaurant = data.restaurants[restaurantId] || data.restaurants['pizza-palace'];

//     // // Update restaurant info
//     // document.getElementById('restaurant-name').textContent = currentRestaurant.name;
//     // document.getElementById('restaurant-location').textContent = currentRestaurant.location;
//     // document.getElementById('restaurant-contact').textContent = currentRestaurant.contact;

//     // Filter food items for this restaurant
//     // const restaurantItems = data.foodItems.filter(item => item.restaurantId === restaurantId);
//     const restaurantItems = data;

//     // Render menu
//     const menuContainer = document.getElementById('menuContent');
//     menuContainer.innerHTML = '';
//     restaurantItems.forEach(item => {
//         const itemCard = `
        
//             <div class="col-md-4 mb-4">
//               <div class="card h-100 shadow-sm">
//                 <div class="card-body">
//                   <h5 class="card-title">${item.name}</h5>
//                   <p class="card-text text-muted">Price: ₹${(item.price)}</p>
//                   <div class="input-group mb-3">
//                     <button class="btn btn-outline-secondary" type="button" onclick="decreaseQty(this)">−</button>
//                     <input type="text" class="form-control text-center" value="1" readonly />
//                     <button class="btn btn-outline-secondary" type="button" onclick="increaseQty(this)">+</button>
//                   </div>
//                   <button class="btn btn-primary w-100 add-to-cart"
//                           data-id="${item.id}"
//                           data-name="${item.name}"
//                           data-price="${item.price}">
//                     Add to Cart
//                   </button>
//                 </div>
//               </div>
//             </div>

//         `;
//         menuContainer.innerHTML += itemCard;
//     });

//     // Initialize cart
//     let cart = JSON.parse(localStorage.getItem('cart')) || [];
//     updateCartCount();

//     // Add to cart listeners
//     setTimeout(() => {
//         document.querySelectorAll('.add-to-cart').forEach(button => {
//             button.addEventListener('click', function () {
//                 const itemId = this.getAttribute('data-id');
//                 const itemName = this.getAttribute('data-name');
//                 const itemPrice = parseFloat(this.getAttribute('data-price'));

//                 const card = this.closest('.card');
//                 const quantity = parseInt(card.querySelector('input').value);

//                 const existingItem = cart.find(item => item.id === itemId);

//                 if (existingItem) {
//                     existingItem.quantity += quantity;
//                 } else {
//                     cart.push({
//                         id: itemId,
//                         name: itemName,
//                         price: itemPrice,
//                         quantity: quantity,
//                         restaurant: currentRestaurant.name
//                     });
//                 }

//                 localStorage.setItem('cart', JSON.stringify(cart));
//                 updateCartCount();

//                 this.innerHTML = '<i class="fas fa-check"></i> Added';
//                 this.classList.remove('btn-primary');
//                 this.classList.add('btn-success');
//                 setTimeout(() => {
//                     this.innerHTML = 'Add to Cart';
//                     this.classList.remove('btn-success');
//                     this.classList.add('btn-primary');
//                 }, 1500);
//             });
//         });
//     }, 0);

//     function updateCartCount() {
//         const totalItems = cart.reduce((total, item) => total + item.quantity, 0);
//         localStorage.setItem('cartCount', totalItems);
//         document.querySelectorAll('.cart-count').forEach(el => {
//             el.textContent = totalItems;
//         });
//     }
// });

// // Quantity functions
// function increaseQty(btn) {
//     const input = btn.parentElement.querySelector('input');
//     input.value = parseInt(input.value) + 1;
// }
// function decreaseQty(btn) {
//     const input = btn.parentElement.querySelector('input');
//     if (parseInt(input.value) > 1) {
//         input.value = parseInt(input.value) - 1;
//     }
// }

