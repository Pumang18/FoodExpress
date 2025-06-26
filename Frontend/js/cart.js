document.addEventListener('DOMContentLoaded', () => {
    // Get cart items from localStorage
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    console.log(cart)
    // Display cart count
    updateCartCount();
    
    // References to DOM elements
    const cartItemsContainer = document.getElementById('cart-items');
    const emptyCartMessage = document.getElementById('empty-cart-message');
    const cartItemsSection = document.getElementById('cart-items-container');
    const subtotalElement = document.getElementById('subtotal');
    const taxElement = document.getElementById('tax');
    const totalElement = document.getElementById('total');
    const restaurantNameElement = document.getElementById('restaurant-name');
    const checkoutForm = document.getElementById('checkout-form');
    const orderSuccessModal = new bootstrap.Modal(document.getElementById('orderSuccessModal'));
    
    
    // Show/hide empty cart message
    if (cart.length === 0) {
        emptyCartMessage.classList.remove('d-none');
        cartItemsSection.classList.add('d-none');
    } else {
        emptyCartMessage.classList.add('d-none');
        cartItemsSection.classList.remove('d-none');
        
        // Display restaurant name
        if (cart.length > 0) {
            restaurantNameElement.textContent = cart[0].restaurant;
        }
        
        // Render cart items
        renderCartItems();
        
        // Calculate and display totals
        updateTotals();
    }
    
    // Event listener for checkout form
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Generate random order number
            const orderNumber = 'ORD-' + Math.floor(100000 + Math.random() * 900000);
            document.getElementById('order-number').textContent = orderNumber;
            
            // Save order to localStorage for order history
            const orders = JSON.parse(localStorage.getItem('orders')) || [];
            orders.push({
                id: orderNumber,
                date: new Date().toISOString(),
                items: cart,
                restaurant: cart[0].restaurant,
                total: calculateTotal(),
                status: 'Pending'
            });
            localStorage.setItem('orders', JSON.stringify(orders));
            
            // Clear cart
            localStorage.removeItem('cart');
            cart = [];
            localStorage.setItem('cartCount', 0);
            
            // Show success modal
            orderSuccessModal.show();
        });
    }
    
    function renderCartItems() {
        if (!cartItemsContainer) return;
        
        cartItemsContainer.innerHTML = '';
        
        cart.forEach((item, index) => {
            const cartItem = document.createElement('div');
            cartItem.className = 'cart-item d-flex align-items-center p-3 border-bottom';
            cartItem.innerHTML = `
                <div class="flex-shrink-0 me-3">
                    <img src="${item.image}" alt="${item.name}" width="60" height="60" class="rounded">
                </div>
                <div class="flex-grow-1">
                    <h6 class="mb-0">${item.name}</h6>
                    <p class="text-muted mb-0">$${item.price.toFixed(2)}</p>
                </div>
                <div class="d-flex align-items-center">
                    <button class="btn btn-sm btn-outline-secondary decrease-quantity" data-index="${index}">-</button>
                    <span class="mx-2">${item.quantity}</span>
                    <button class="btn btn-sm btn-outline-secondary increase-quantity" data-index="${index}">+</button>
                    <button class="btn btn-sm btn-outline-danger ms-3 remove-item" data-index="${index}">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </div>
                <div class="ms-3 text-end">
                    <span class="fw-bold">$${(item.price * item.quantity).toFixed(2)}</span>
                </div>
            `;
            cartItemsContainer.appendChild(cartItem);
        });
        
        // Add event listeners to buttons
        document.querySelectorAll('.increase-quantity').forEach(button => {
            button.addEventListener('click', function() {
                const index = parseInt(this.getAttribute('data-index'));
                cart[index].quantity += 1;
                updateCart();
            });
        });
        
        document.querySelectorAll('.decrease-quantity').forEach(button => {
            button.addEventListener('click', function() {
                const index = parseInt(this.getAttribute('data-index'));
                if (cart[index].quantity > 1) {
                    cart[index].quantity -= 1;
                    updateCart();
                }
            });
        });
        
        document.querySelectorAll('.remove-item').forEach(button => {
            button.addEventListener('click', function() {
                const index = parseInt(this.getAttribute('data-index'));
                cart.splice(index, 1);
                
                if (cart.length === 0) {
                    emptyCartMessage.classList.remove('d-none');
                    cartItemsSection.classList.add('d-none');
                } else {
                    restaurantNameElement.textContent = cart[0].restaurant;
                }
                
                updateCart();
            });
        });
    }
    
    function updateCart() {
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCartItems();
        updateTotals();
        updateCartCount();
    }
    
    
    function updateTotals() {
        const subtotal = cart.reduce((total, item) => total + (item.price * item.quantity), 0);
        const deliveryFee = 2.99;
        const tax = subtotal * 0.08; // 8% tax
        const total = subtotal + deliveryFee + tax;
        
        subtotalElement.textContent = `$${subtotal.toFixed(2)}`;
        taxElement.textContent = `$${tax.toFixed(2)}`;
        totalElement.textContent = `$${total.toFixed(2)}`;
    }
    
    function calculateTotal() {
        const subtotal = cart.reduce((total, item) => total + (item.price * item.quantity), 0);
        const deliveryFee = 2.99;
        const tax = subtotal * 0.08; // 8% tax
        return (subtotal + deliveryFee + tax).toFixed(2);
    }
    
    function updateCartCount() {
        const totalItems = cart.reduce((total, item) => total + item.quantity, 0);
        localStorage.setItem('cartCount', totalItems);
        document.querySelectorAll('.cart-count').forEach(el => {
            el.textContent = totalItems;
        });
    }
});
