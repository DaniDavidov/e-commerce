const minusBtn = document.getElementById('minusBtn');
const plusBtn = document.getElementById('plusBtn');
const quantityInputField = document.getElementById('quantity');
const addToCartForm = document.getElementById('addToCartForm');
const bookIdInputField = document.getElementById('offerId');
const addToCartSection = document.getElementById('addToCartSection');
const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

async function buyBook(event) {
    event.preventDefault();

    const requestBody = {
        offerId: bookIdInputField.value,
        quantity: quantityInputField.value
    };

    function getConfirmMessage(data) {
        const p = document.createElement('p');
        p.textContent = `${data.quantity} x ${data.offerName} added to cart`;
        return p;
    }

    fetch('http://localhost:8050/api/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify(requestBody)
    }).then(res => res.json())
        .then(data => {
            addToCartSection.appendChild(getConfirmMessage(data));
        });
}

addToCartForm.addEventListener('submit', buyBook);

function decreaseQuantity() {
    let newValue = Number(Number(quantityInputField.value) - 1);
    if (newValue <= 0) {
        newValue = 1;
    }

    quantityInputField.value = newValue;
}

minusBtn.addEventListener('click', decreaseQuantity);

function increaseQuantity() {
    quantityInputField.value = Number(Number(quantityInputField.value) + 1);
}

plusBtn.addEventListener('click', increaseQuantity);