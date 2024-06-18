document.addEventListener("DOMContentLoaded", function() {

    fetchAndUpdatePromotions('2for3', 'twoForThreeProducts');
    fetchAndUpdatePromotions('buy1get1half', 'oneAndHalfProducts');

    document.getElementById('create-promotion').addEventListener('click', function() {
        openProductModal('2for3');
    });

    document.getElementById('add-promotion').addEventListener('click', function() {
        openProductModal('buy1get1half');
    });

    function openProductModal(promotionType) {
        const modal = new bootstrap.Modal(document.getElementById('productModal'));
        const modalTitle = document.getElementById('productModalLabel');
        const modalError = document.getElementById('productModalError');
        const saveButton = document.getElementById('saveProductsBtn');
        const productNamesInput = document.getElementById('productNames');

        productNamesInput.value = '';
        modalError.classList.add('d-none');

        if (promotionType === '2for3') {
            modalTitle.textContent = 'Enter Three Products';
        } else if (promotionType === 'buy1get1half') {
            modalTitle.textContent = 'Enter One Product';
        }

        modal.show();

        saveButton.addEventListener('click', async function() {
            const productNames = productNamesInput.value.trim().split(',').map(name => name.trim());

            const promotionDto = { products: productNames };

            try {
                const response = await fetch(`/promotions/${promotionType}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(promotionDto)
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    displayPromotionErrors(errorData.fieldWithErrors || []);
                    throw new Error('Failed to create promotion');
                }

                fetchAndUpdatePromotions(promotionType, promotionType === '2for3' ? 'twoForThreeProducts' : 'oneAndHalfProducts');

                modal.hide();
            } catch (error) {
                console.error(`Error creating ${promotionType} promotion:`, error);
            }
        });
    }

    function displayPromotionErrors(errors) {
        const modalError = document.getElementById('productModalError');
        modalError.textContent = errors.join(', ');
        modalError.classList.remove('d-none');
    }

    async function fetchAndUpdatePromotions(promotionType, targetElementId) {
        try {
            const response = await fetch(`/promotions/${promotionType}`);
            if (!response.ok) {
                throw new Error(`Failed to fetch ${promotionType} promotion`);
            }
            const data = await response.json();
            const products = data.products.join(', ');
            document.getElementById(targetElementId).textContent = products;
        } catch (error) {
            console.error(`Error fetching ${promotionType} promotion:`, error);
        }
    }
});