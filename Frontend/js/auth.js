document.addEventListener('DOMContentLoaded', () => {
    const customerLoginForm = document.getElementById('customerLoginForm');
    const ownerLoginForm = document.getElementById('ownerLoginForm');
    const adminLoginForm = document.getElementById('adminLoginForm');
    
    if (customerLoginForm) {
        customerLoginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = document.getElementById('customerEmail').value;
            const password = document.getElementById('customerPassword').value;

  
            if (email === 'customer@example.com' && password === 'customer') {
                window.location.href = './pages/customer/home.html';
            } else {
                alert('Invalid credentials');
            }
        });
    }
    
    if (ownerLoginForm) {
        ownerLoginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = document.getElementById('ownerEmail').value;
            const password = document.getElementById('ownerPassword').value;

    
            if (email === 'owner@example.com' && password === 'owner') {
                window.location.href = './pages/owner/dashboard.html';
            } else {
                alert('Invalid credentials');
            }
        });
    }
    
    if (adminLoginForm) {
        adminLoginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = document.getElementById('adminEmail').value;
            const password = document.getElementById('adminPassword').value;

            if (email === 'admin@example.com' && password === 'admin') {
                window.location.href = '/pages/admin/dashboard.html';
            } else {
                alert('Invalid credentials');
            }
        });
    }
});
