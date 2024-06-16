document.addEventListener('DOMContentLoaded', function() {
    const productContainer = document.getElementById('product-list');
    const selectedProductsContainer = document.getElementById('selected-products');
    const calculateButton = document.getElementById('calculate-btn');
    const totalPriceContainer = document.getElementById('total-price');
    const updateProductModal = new bootstrap.Modal(document.getElementById('updateProductModal'));
    const updateButton = document.getElementById('updateProductButton');

    let currentProductId;

    async function fetchProducts() {
        try {
            const response = await fetch('http://localhost:8080/products');
            if (!response.ok) {
                throw new Error('Failed to fetch products');
            }
            const productList = await response.json();
            renderProductList(productList);
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    }

    function renderProductList(productList) {
        productList.forEach(product => {
            const li = document.createElement('li');
            li.innerHTML = `
                ${product.name} - ${product.price} eur
                <button class="add-btn">+</button>
                <button class="update-btn">U</button>
                <button class="delete-btn">D</button>
            `;

            const addButton = li.querySelector('.add-btn');
            addButton.addEventListener('click', function() {
                scanProduct(product.name);
            });

            const updateButton = li.querySelector('.update-btn');
            updateButton.addEventListener('click', function() {
                openUpdateModal(product.id, product.name, product.price);
            });

            const deleteButton = li.querySelector('.delete-btn');
            deleteButton.addEventListener('click', function() {
                deleteProduct(product.id);
            });

            productContainer.appendChild(li);
        });
    }

    function scanProduct(name) {
        const div = document.createElement('div');
        div.classList.add('selected-product');
        div.innerHTML = `
            <span>${name}</span>
            <button class="remove-btn">x</button>
        `;
        div.setAttribute('data-name', name);
        const removeButton = div.querySelector('.remove-btn');
        removeButton.addEventListener('click', function() {
            div.remove();
        });
        selectedProductsContainer.appendChild(div);
    }

    function openUpdateModal(id, name, price) {
        currentProductId = id;
        document.getElementById('productName').value = name;
        document.getElementById('productPrice').value = price;
        updateProductModal.show();
    }

    updateButton.addEventListener('click', async function() {
        const productName = document.getElementById('productName').value;
        const productPrice = document.getElementById('productPrice').value;

        try {
            const response = await fetch(`http://localhost:8080/products/edit/${currentProductId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id: currentProductId,
                    name: productName,
                    price: parseFloat(productPrice),
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                displayErrors(errorData.fieldWithErrors || []);
                throw new Error('Failed to update product');
            }

            updateProductModal.hide();
            productContainer.innerHTML = '';
            fetchProducts();
        } catch (error) {
            console.error('Error updating product:', error);
        }
    });

    function displayErrors(errors) {
        const errorContainer = document.getElementById('updateProductError');
        errorContainer.innerHTML = '';

        if (errors.length > 0) {
            const errorMessages = errors.join(' ');
            errorContainer.textContent = errorMessages;
        } else {
            errorContainer.textContent = 'An unknown error occurred.';
        }

        errorContainer.style.display = 'block';
    }

    function deleteProduct(id) {
        fetch(`http://localhost:8080/products/delete/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete product');
                }
                productContainer.innerHTML = '';
                fetchProducts();
            })
            .catch(error => {
                console.error('Error deleting product:', error);
            });
    }

    calculateButton.addEventListener('click', function() {
        const selectedProductNames = Array.from(selectedProductsContainer.querySelectorAll('.selected-product'))
            .map(div => div.getAttribute('data-name'));

        fetch('http://localhost:8080/price', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(selectedProductNames),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch total price');
                }
                return response.json();
            })
            .then(data => {
                console.log("Total price:", data);
                totalPriceContainer.textContent = `Total Price: ${data} eur`;
            })
            .catch(error => {
                console.error('Error calculating total price:', error);
            });
    });

    fetchProducts();
});