"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
function addToCart(listingId) {
    return __awaiter(this, void 0, void 0, function* () {
        const quantityInput = document.getElementById('quantity-' + listingId.toString());
        const quantity = quantityInput.value;
        try {
            const response = yield fetch('/api/cart/add-to-cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    listingId: listingId,
                    quantity: quantity
                })
            });
            if (response.ok) {
                showMessage('Item added to cart! :3');
            }
            else {
                showMessage('Could not add item to cart :c');
            }
        }
        catch (_a) {
            showMessage('Could not add item to cart :c');
        }
    });
}
function removeFromCart(listingId) {
    return __awaiter(this, void 0, void 0, function* () {
        const listingDiv = document.getElementById('cart-item-' + listingId);
        try {
            const response = yield fetch('/api/cart/remove/' + listingId, {
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
                listingDiv === null || listingDiv === void 0 ? void 0 : listingDiv.remove();
                const listingDivs = document.getElementsByClassName('cart-item');
                if (listingDivs.length === 0) {
                    const checkoutButton = document.getElementById('checkout-form');
                    const noItemsMessage = document.getElementById('no-items-message');
                    checkoutButton.remove();
                    noItemsMessage.classList.remove('hidden');
                    noItemsMessage.classList.add('absolute');
                }
            }
            else {
                showMessage('Could not remove item from cart :c');
            }
        }
        catch (_a) {
            showMessage('Could not remove item from cart :c');
        }
    });
}
function updateQuantity(listingId, quantity) {
    return __awaiter(this, void 0, void 0, function* () {
        try {
            const response = yield fetch('/api/cart/update-quantity', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    listingId: listingId,
                    quantity: quantity
                })
            });
            if (response.ok) {
            }
            else {
                showMessage('Could not update quantity');
            }
        }
        catch (_a) {
            showMessage('Could not update quantity');
        }
    });
}
function showMessage(message) {
    const msg = document.createElement('div');
    msg.classList.add('self-center');
    msg.classList.add('fixed');
    msg.innerHTML = `<div class="opacity-100 max-w-fit transition-all duration-1000">
    <p class="p-4 pl-8 pr-8 bg-slate-100 text-tertiary rounded-sm">${message}</p>
    </div>`;
    const main = document.querySelector('main');
    const appendedDiv = main === null || main === void 0 ? void 0 : main.appendChild(msg);
    const appendedMsgDiv = appendedDiv.firstChild;
    appendedMsgDiv.addEventListener('click', (event) => {
        appendedMsgDiv.classList.add('opacity-0');
    });
    appendedMsgDiv.addEventListener('animationend', (event) => {
        appendedMsgDiv.remove();
    });
    setTimeout(() => {
        appendedMsgDiv.classList.remove('opacity-100');
        appendedMsgDiv.classList.add('opacity-0');
    }, 2000);
}
