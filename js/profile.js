document.addEventListener('DOMContentLoaded', () => {
    // Display cart count
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const cartCount = cart.reduce((total, item) => total + item.quantity, 0);
    document.querySelectorAll('.cart-count').forEach(el => {
        el.textContent = cartCount;
    });
    
    // Get orders from localStorage
    let orders = JSON.parse(localStorage.getItem('orders')) || [];
    
    // DOM elements
    const ordersContainer = document.getElementById('orders-container');
    const noOrdersMessage = document.getElementById('no-orders-message');
    const filterStatus = document.getElementById('filter-status');
    const filterTime = document.getElementById('filter-time');
    const orderDetailsModal = new bootstrap.Modal(document.getElementById('orderDetailsModal'));
    const orderDetailsContent = document.getElementById('order-details-content');
    const saveProfileButton = document.getElementById('save-profile');
    
    // Show/hide no orders message
    if (orders.length === 0) {
        if (noOrdersMessage) noOrdersMessage.classList.remove('d-none');
        if (ordersContainer) ordersContainer.classList.add('d-none');
    } else {
        if (noOrdersMessage) noOrdersMessage.classList.add('d-none');
        if (ordersContainer) ordersContainer.classList.remove('d-none');
        
        // Display orders
        renderOrders(orders);
    }
    
    // Event listeners for filters
    if (filterStatus) {
        filterStatus.addEventListener('change', applyFilters);
    }
    
    if (filterTime) {
        filterTime.addEventListener('change', applyFilters);
    }
    
    // Event listener for save profile button
    if (saveProfileButton) {
        saveProfileButton.addEventListener('click', function() {
            // Here you would typically send the updated profile data to a server
            // For this demo, we'll just show a success message
            alert('Profile updated successfully!');
            
            // Close the modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('editProfileModal'));
            modal.hide();
        });
    }
    
    function renderOrders(ordersToRender) {
        if (!ordersContainer) return;
        
        ordersContainer.innerHTML = '';
        
        ordersToRender.forEach(order => {
            // Format date
            const orderDate = new Date(order.date);
            const formattedDate = orderDate.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'short',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
            
            // Get status color
            let statusColor = '';
            switch(order.status) {
                case 'Pending':
                    statusColor = 'status-pending';
                    break;
                case 'Completed':
                    statusColor = 'status-completed';
                    break;
                case 'Cancelled':
                    statusColor = 'status-cancelled';
                    break;
                default:
                    statusColor = '';
            }
            
            // Calculate total items
            const totalItems = order.items.reduce((total, item) => total + item.quantity, 0);
            
            const orderElement = document.createElement('div');
            orderElement.className = 'border-bottom p-3';
            orderElement.innerHTML = `
                <div class="row align-items-center">
                    <div class="col-md-3">
                        <p class="mb-1 small text-muted">Order ID</p>
                        <p class="mb-0 small fw-bold">${order.id}</p>
                    </div>
                    <div class="col-md-3">
                        <p class="mb-1 small text-muted">Date</p>
                        <p class="mb-0 small">${formattedDate}</p>
                    </div>
                    <div class="col-md-2">
                        <p class="mb-1 small text-muted">Restaurant</p>
                        <p class="mb-0 small">${order.restaurant}</p>
                    </div>
                    <div class="col-md-2">
                        <p class="mb-1 small text-muted">Amount</p>
                        <p class="mb-0 fw-bold">$${order.total}</p>
                    </div>
                    <div class="col-md-2 text-md-end">
                        <span class="badge rounded-pill bg-light text-dark mb-2">${totalItems} item${totalItems !== 1 ? 's' : ''}</span>
                        <div class="d-flex justify-content-md-end align-items-center">
                            <span class="order-status ${statusColor} me-3">${order.status}</span>
                            <button class="btn btn-sm btn-outline-primary view-order" data-order-id="${order.id}">View</button>
                        </div>
                    </div>
                </div>
            `;
            ordersContainer.appendChild(orderElement);
        });
        
        // Add event listeners to view buttons
        document.querySelectorAll('.view-order').forEach(button => {
            button.addEventListener('click', function() {
                const orderId = this.getAttribute('data-order-id');
                showOrderDetails(orderId);
            });
        });
    }
    
    function showOrderDetails(orderId) {
        const order = orders.find(o => o.id === orderId);
        
        if (!order || !orderDetailsContent) return;
        
        // Format date
        const orderDate = new Date(order.date);
        const formattedDate = orderDate.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        
        // Calculate subtotal
        const subtotal = order.items.reduce((total, item) => total + (item.price * item.quantity), 0);
        const deliveryFee = 2.99;
        const tax = subtotal * 0.08; // 8% tax
        
        // Generate items HTML
        let itemsHtml = '';
        order.items.forEach(item => {
            itemsHtml += `
                <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                    <div class="d-flex align-items-center">
                        <img src="${item.image}" alt="${item.name}" width="50" height="50" class="rounded me-3">
                        <div>
                            <h6 class="mb-0">${item.name}</h6>
                            <p class="text-muted mb-0 small">$${item.price.toFixed(2)} x ${item.quantity}</p>
                        </div>
                    </div>
                    <div class="fw-bold">$${(item.price * item.quantity).toFixed(2)}</div>
                </div>
            `;
        });
        
        // Set modal content
        orderDetailsContent.innerHTML = `
            <div class="row mb-4">
                <div class="col-md-6">
                    <h6 class="text-muted">Order Information</h6>
                    <p class="mb-1"><strong>Order ID:</strong> ${order.id}</p>
                    <p class="mb-1"><strong>Date:</strong> ${formattedDate}</p>
                    <p class="mb-1"><strong>Restaurant:</strong> ${order.restaurant}</p>
                    <p class="mb-0"><strong>Status:</strong> <span class="order-status ${order.status === 'Pending' ? 'status-pending' : order.status === 'Completed' ? 'status-completed' : 'status-cancelled'}">${order.status}</span></p>
                </div>
                <div class="col-md-6">
                    <h6 class="text-muted">Delivery Information</h6>
                    <p class="mb-1"><strong>Address:</strong> 123 Main St, Anytown, USA</p>
                    <p class="mb-1"><strong>Phone:</strong> (123) 456-7890</p>
                    <p class="mb-0"><strong>Payment Method:</strong> Cash on Delivery</p>
                </div>
            </div>
            
            <h6 class="text-muted mb-3">Order Items</h6>
            <div class="mb-4">
                ${itemsHtml}
            </div>
            
            <div class="row">
                <div class="col-md-6 offset-md-6">
                    <div class="d-flex justify-content-between mb-2">
                        <span>Subtotal:</span>
                        <span>$${subtotal.toFixed(2)}</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Delivery Fee:</span>
                        <span>$${deliveryFee.toFixed(2)}</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Tax:</span>
                        <span>$${tax.toFixed(2)}</span>
                    </div>
                    <hr>
                    <div class="d-flex justify-content-between fw-bold">
                        <span>Total:</span>
                        <span>$${order.total}</span>
                    </div>
                </div>
            </div>
        `;
        
        // Show modal
        orderDetailsModal.show();
    }
    
    function applyFilters() {
        if (!filterStatus || !filterTime) return;
        
        const statusFilter = filterStatus.value;
        const timeFilter = filterTime.value;
        
        let filteredOrders = [...orders];
        
        // Apply status filter
        if (statusFilter !== 'all') {
            filteredOrders = filteredOrders.filter(order => order.status === statusFilter);
        }
        
        // Apply time filter
        if (timeFilter !== 'all') {
            const now = new Date();
            const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            const thisWeekStart = new Date(today);
            thisWeekStart.setDate(thisWeekStart.getDate() - thisWeekStart.getDay());
            const thisMonthStart = new Date(now.getFullYear(), now.getMonth(), 1);
            
            filteredOrders = filteredOrders.filter(order => {
                const orderDate = new Date(order.date);
                
                switch(timeFilter) {
                    case 'today':
                        return orderDate >= today;
                    case 'week':
                        return orderDate >= thisWeekStart;
                    case 'month':
                        return orderDate >= thisMonthStart;
                    default:
                        return true;
                }
            });
        }
        
        // Check if there are orders after filtering
        if (filteredOrders.length === 0) {
            if (noOrdersMessage) noOrdersMessage.classList.remove('d-none');
            if (ordersContainer) ordersContainer.classList.add('d-none');
        } else {
            if (noOrdersMessage) noOrdersMessage.classList.add('d-none');
            if (ordersContainer) ordersContainer.classList.remove('d-none');
            renderOrders(filteredOrders);
        }
    }
    
    // Add some sample orders if none exist
    if (orders.length === 0) {
        const pastWeek = new Date();
        pastWeek.setDate(pastWeek.getDate() - 7);
        
        const yesterday = new Date();
        yesterday.setDate(yesterday.getDate() - 1);
        
        orders = [
            {
                id: 'ORD-123456',
                date: yesterday.toISOString(),
                restaurant: 'Pizza Palace',
                items: [
                    { id: 'm1', name: 'Margherita Pizza', price: 12.99, quantity: 1, image: 'https://via.placeholder.com/150?text=Margherita' },
                    { id: 'a1', name: 'Garlic Bread', price: 4.99, quantity: 1, image: 'https://via.placeholder.com/150?text=Garlic+Bread' }
                ],
                total: '19.74',
                status: 'Completed'
            },
            {
                id: 'ORD-654321',
                date: pastWeek.toISOString(),
                restaurant: 'Burger Hub',
                items: [
                    { id: 'm1', name: 'Classic Burger', price: 10.99, quantity: 2, image: 'https://via.placeholder.com/150?text=Classic+Burger' },
                    { id: 'dr1', name: 'Soft Drink', price: 2.49, quantity: 2, image: 'https://via.placeholder.com/150?text=Soft+Drink' }
                ],
                total: '28.94',
                status: 'Pending'
            }
        ];
        
        localStorage.setItem('orders', JSON.stringify(orders));
        
        if (noOrdersMessage) noOrdersMessage.classList.add('d-none');
        if (ordersContainer) ordersContainer.classList.remove('d-none');
        renderOrders(orders);
    }
});
