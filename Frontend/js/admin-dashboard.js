document.addEventListener('DOMContentLoaded', () => {
    // Orders and Revenue chart
    const ordersRevenueCtx = document.getElementById('ordersRevenueChart');
    if (ordersRevenueCtx) {
        const ordersRevenueChart = new Chart(ordersRevenueCtx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                    label: 'Orders',
                    data: [520, 550, 620, 680, 720, 800, 850, 900, 950, 1000, 1100, 1200],
                    fill: false,
                    borderColor: '#5a67d8',
                    tension: 0.1
                }, {
                    label: 'Revenue ($K)',
                    data: [15, 16, 18, 19, 20, 22, 23, 24, 25, 26, 28, 30],
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
    
    // User Types chart
    const userTypesCtx = document.getElementById('userTypesChart');
    if (userTypesCtx) {
        const userTypesChart = new Chart(userTypesCtx, {
            type: 'doughnut',
            data: {
                labels: ['Customers', 'Restaurant Owners'],
                datasets: [{
                    data: [75, 15],
                    backgroundColor: [
                        '#5a67d8',
                        '#48bb78'
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
    
    // Top Restaurants chart
    const topRestaurantsCtx = document.getElementById('topRestaurantsChart');
    if (topRestaurantsCtx) {
        const topRestaurantsChart = new Chart(topRestaurantsCtx, {
            type: 'bar',
            data: {
                labels: ['Pizza Palace', 'Burger Hub', 'Sushi Lounge', 'Taco Express', 'Indian Spice'],
                datasets: [{
                    label: 'Orders',
                    data: [458, 389, 325, 287, 246],
                    backgroundColor: '#5a67d8'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: 'y',
                scales: {
                    x: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
    
        
    
});
