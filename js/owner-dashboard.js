document.addEventListener('DOMContentLoaded', () => {
    // Orders chart
    const ordersCtx = document.getElementById('ordersChart');
    if (ordersCtx) {
        const ordersChart = new Chart(ordersCtx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                    label: 'Orders',
                    data: [65, 59, 80, 81, 56, 55, 70, 75, 82, 95, 120, 135],
                    fill: false,
                    borderColor: '#5a67d8',
                    tension: 0.1
                }, {
                    label: 'Revenue ($)',
                    data: [1800, 1600, 2100, 2200, 1500, 1450, 1900, 2000, 2200, 2500, 3200, 3600],
                    fill: false,
                    borderColor: '#48bb78',
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
    
    // Categories chart
    const categoriesCtx = document.getElementById('categoriesChart');
    if (categoriesCtx) {
        const categoriesChart = new Chart(categoriesCtx, {
            type: 'doughnut',
            data: {
                labels: ['Main Courses', 'Appetizers', 'Desserts', 'Drinks'],
                datasets: [{
                    data: [65, 15, 12, 8],
                    backgroundColor: [
                        '#5a67d8',
                        '#48bb78',
                        '#ed8936',
                        '#4299e1'
                    ]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
    
    // Payment methods chart
    const paymentCtx = document.getElementById('paymentChart');
    if (paymentCtx) {
        const paymentChart = new Chart(paymentCtx, {
            type: 'pie',
            data: {
                labels: ['Credit/Debit Card', 'Cash on Delivery', 'Digital Wallet'],
                datasets: [{
                    data: [60, 30, 10],
                    backgroundColor: [
                        '#5a67d8',
                        '#48bb78',
                        '#4299e1'
                    ]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
    
    // Sales by day chart
    const salesByDayCtx = document.getElementById('salesByDayChart');
    if (salesByDayCtx) {
        const salesByDayChart = new Chart(salesByDayCtx, {
            type: 'bar',
            data: {
                labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                datasets: [{
                    label: 'Sales ($)',
                    data: [450, 470, 580, 650, 850, 950, 750],
                    backgroundColor: '#5a67d8'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
});
