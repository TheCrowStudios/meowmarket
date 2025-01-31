async function addToCart(listingId: Number) {
    try {
        const response = await fetch('/api/cart/add-to-cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                listingId: listingId,
                quantity: 1
            })
        });

        if (response.ok) {
            showMessage('Item added to cart! :3');
        } else {
            showMessage('Could not add item to cart :c')
        }
    } catch {
        showMessage('Could not add item to cart :c')
    }
}

async function removeFromCart(listingId: Number) {
    const listingDiv = document.getElementById('cart-item-' + listingId);

    try {
        const response = await fetch('/api/cart/remove/' + listingId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                listingId: listingId,
            })
        });

        if (response.ok) {
            showMessage('Item removed from cart');
            listingDiv?.remove();
        } else {
            showMessage('Could not remove item from cart :c')
        }
    } catch {
        showMessage('Could not remove item from cart :c')
    }
}

function showMessage(message: string) {
    const msg = document.createElement('div');
    msg.classList.add('self-center');
    msg.classList.add('absolute');
    msg.innerHTML = `<div class="opacity-100 max-w-fit transition-all duration-1000">
    <p class="p-4 pl-8 pr-8 bg-slate-100 text-tertiary rounded-sm">${message}</p>
    </div>`;
    const main = document.querySelector('main');
    const appendedDiv = main?.appendChild(msg) as HTMLDivElement;
    const appendedMsgDiv = appendedDiv.firstChild as HTMLDivElement;

    appendedMsgDiv.addEventListener('click', (event) => {
        appendedMsgDiv.classList.add('opacity-0');
    })

    appendedMsgDiv.addEventListener('animationend', (event) => {
        appendedMsgDiv.remove();
    })

    setTimeout(() => {
        appendedMsgDiv.classList.add('opacity-0');
    }, 2000);
}